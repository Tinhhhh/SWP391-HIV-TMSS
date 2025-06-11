package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.request.UpdateBlog;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByManager;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;


    @Operation(summary = "Create Blog By Account", description = "Create Blog By Account")
    @PostMapping
    public ResponseEntity<Object> createBlog(@Valid @RequestBody BlogRequest blogRequest) {
        blogService.createBlog(blogRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your Blog created successfully");
    }

    @Operation(summary = "Get Blog By BlogID", description = "Get Blog By BlogID")
    @GetMapping
    public ResponseEntity<Object> getBlogById(@PathParam("id") Long id) {

        return ResponseBuilder.returnData(HttpStatus.OK, "Get BlogByID Successfully",
                blogService.getBlogById(id));
    }

    @Operation(summary = "Get Blog By AccountID", description = "Get Blog By AccountID")
    @GetMapping("/account")
    public ResponseEntity<Object> getBlogByAccountId(@PathParam("accountId") UUID accountId) {

        List<BlogResponse> blogResponseList = blogService.getBlogByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Get Blog By AccountID Successfully",
                blogResponseList);
    }

    @Operation(summary = "Get All Blog ", description = "Get All Blog")
    @GetMapping("/all")
    public ResponseEntity<List<BlogResponse>> getAllBlog() {
        List<BlogResponse> blog = blogService.getAllBlogs();
        return ResponseEntity.ok(blog);
    }

    @Operation(summary = "Update Blog By ID", description = "Get Blog By ID")
    @PutMapping
    public ResponseEntity<Object> updateBlog(@PathParam("id") Long id,@Valid @RequestBody UpdateBlog updateBlog) {
        blogService.updateBlog(id, updateBlog);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Update Blog Successfully");
    }

    @Operation(summary = "Delete Blog", description = "Delete Blog")
    @DeleteMapping
    public ResponseEntity<Object> deleteBlog(@PathParam("id") Long id,
                                             @Valid @RequestBody UpdateBlogByManager updateBlogByManager) {
        // delete blog by change blog status , not delete all information
        blogService.deleteBlog(id, updateBlogByManager);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete Blog Successfully");
    }

}
