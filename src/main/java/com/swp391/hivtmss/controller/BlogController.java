package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @PostMapping
    public ResponseEntity<BlogResponse> createBlog(@Valid @RequestBody BlogRequest blogRequest){
        BlogResponse createBlog = blogService.createBlog(blogRequest);
        return new ResponseEntity<>(createBlog, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getDoctorDegreeById(@PathVariable Long id) {
        BlogResponse doctorDegree = blogService.getBlogById(id);
        return ResponseEntity.ok(doctorDegree);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<BlogResponse> getDoctorDegreeByAccountId(@PathVariable UUID accountId) {
        BlogResponse doctorDegree = blogService.getBlogByAccountId(accountId);
        return ResponseEntity.ok(doctorDegree);
    }

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getAllDoctorDegrees() {
        List<BlogResponse> degrees = blogService.getAllBlogs();
        return ResponseEntity.ok(degrees);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> updateDoctorDegree(@PathVariable Long id,
                                                                   @Valid @RequestBody BlogRequest blogRequest) {
        BlogResponse updatedDegree = blogService.updateBlog(id, blogRequest);
        return ResponseEntity.ok(updatedDegree);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorDegree(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }

}
