package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.*;
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
import static com.swp391.hivtmss.util.AppConstants.*;

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

    @Operation(summary = "Get All Blogs By AccountID", description = "Get All Blogs By AccountID")
    @GetMapping("/account")
    public ResponseEntity<Object> getBlogByAccountId(@PathParam("accountId") UUID accountId) {

        List<BlogResponse> blogResponseList = blogService.getBlogByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Get All Blog By AccountID Successfully",
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
                                             @Valid @RequestBody UpdateBlogByCustomer updateBlogByCustomer) {
        // delete blog by change blog status , not delete all information
        blogService.deleteBlog(id, updateBlogByCustomer);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete Blog Successfully");
    }


    @Operation(summary = "Update Blogs status with account:Manager ", description = "Update the Blog status By Role. Role required: MANAGER")
    @PutMapping("/approved")
    public ResponseEntity<Object> updateBlogByRole(
            @RequestBody UpdateBlogByManager updateBlogByManager) {

        blogService.updateBlogByManager(updateBlogByManager);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Blog Status approved by Manager successfully");
    }

    @Operation(summary = "Rejected Blog status ", description = "Reject the status of an Blog. Role required: MANAGER")
    @PutMapping("/rejected")
    public ResponseEntity<Object> cancelAppointment(@RequestParam("id") Long id) {
        blogService.cancelBlog(id);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Blog Status rejected successfully");
    }

    @Operation(summary = "Get all blog info", description = "get all blogs info")
    @GetMapping("/blog/all")
    public ResponseEntity<Object> getBlogs(
            @RequestParam(value = "pageNo", defaultValue = DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = DEFAULT_SORT_DIRECTION) String sortDir,
            @RequestParam("searchTerm") String searchTerm) {
        return ResponseBuilder.returnData(
                HttpStatus.OK, "Successfully retrieved Blog for customer",
                blogService.getAllBlog(pageNo, pageSize, sortBy, sortDir, searchTerm));
    }


}
