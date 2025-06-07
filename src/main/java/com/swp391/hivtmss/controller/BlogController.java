package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.service.BlogService;
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

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest blogRequest){
        BlogResponse createBlog = blogService.createBlog(blogRequest);
        return new ResponseEntity<>(createBlog, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BlogResponse> getBlogById(@PathParam("id") Long id) {
        BlogResponse blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }

    @GetMapping("/account")
    public ResponseEntity<BlogResponse> getBlogByAccountId(@PathParam("accountId") UUID accountId) {
        BlogResponse blog = blogService.getBlogByAccountId(accountId);
        return ResponseEntity.ok(blog);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllBlog() {
        List<BlogResponse> blog = blogService.getAllBlogs();
        return ResponseEntity.ok(blog);
    }

    @PutMapping
    public ResponseEntity<BlogResponse> updateBlog(@PathParam("/{id}") Long id,
                                                                   @Valid @RequestBody BlogRequest blogRequest) {
        BlogResponse updatedBlog = blogService.updateBlog(id, blogRequest);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBlog(@PathParam("id") Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

}
