package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Operation(summary = "Create Blog By Account", description = "Create Blog By Account")
    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest blogRequest){
        BlogResponse createBlog = blogService.createBlog(blogRequest);
        return new ResponseEntity<>(createBlog, HttpStatus.CREATED);
    }

    @Operation(summary = "Get Blog By BlogID", description = "Get Blog By BlogID")
    @GetMapping
    public ResponseEntity<Object> getBlogById(@PathParam("id") Long id) {
        BlogResponse blog = blogService.getBlogById(id);
        return ResponseBuilder.returnData(HttpStatus.OK, "Get BlogByID Successfully", blog);
    }

    @Operation(summary = "Get Blog By AccountID", description = "Get Blog By AccountID")
    @GetMapping("/account")
    public ResponseEntity<Object> getBlogByAccountId(@PathParam("accountId") UUID accountId) {
        BlogResponse blog = blogService.getBlogByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Get Blog By AccountID Successfully", blog);
    }

    @Operation(summary = "Get All Blog ", description = "Get All Blog")
    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlog() {
        List<BlogResponse> blog = blogService.getAllBlogs();
        return ResponseEntity.ok(blog);
    }

    @Operation(summary = "Update Blog By ID", description = "Get Blog By ID")
    @PutMapping
    public ResponseEntity<Object> updateBlog(@PathParam("id") Long id,
                                                                   @Valid @RequestBody BlogRequest blogRequest) {
        BlogResponse updatedBlog = blogService.updateBlog(id, blogRequest);
        return ResponseBuilder.returnData(HttpStatus.OK, "Update Blog Successfully", updatedBlog);
    }
    @Operation(summary = "Delete Blog", description = "Delete Blog")
    @DeleteMapping
    public ResponseEntity<Object> deleteBlog(@PathParam("id") Long id,
                                             @Valid @RequestBody BlogRequest blogRequest) {
        // delete blog by change blogStatus , not delete all information
        BlogResponse deleteBlog = blogService.deleteBlog(id, blogRequest);
        return ResponseBuilder.returnData(HttpStatus.OK, "Delete Blog Successfully", deleteBlog);
    }

}
