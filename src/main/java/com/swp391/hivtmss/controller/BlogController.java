package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.*;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.model.payload.response.DoctorDegreeResponse;
import com.swp391.hivtmss.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import static com.swp391.hivtmss.util.AppConstants.*;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;


    @Operation(summary = "Create Blog By Account", description = "Create Blog By Account")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createBlog(
            @RequestPart("title") String title,
            @RequestPart("content") String content,
            @RequestPart("accountID") String accountID,
            @RequestPart("files") List<MultipartFile> files
    ) {
        BlogRequest blogRequest = new BlogRequest(title, content, UUID.fromString(accountID));
        blogService.createBlog(blogRequest, files);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Create Blog Successfully");
    }

    @Operation(summary = "Get Blog By BlogID", description = "Get Blog By BlogID")
    @GetMapping
    public ResponseEntity<Object> getBlogById(@RequestParam("id") Long id) {

        return ResponseBuilder.returnData(HttpStatus.OK, "Get BlogByID Successfully",
                blogService.getBlogById(id));
    }

    @Operation(summary = "Get All Blogs By AccountID", description = "Get All Blogs By AccountID")
    @GetMapping("/account")
    public ResponseEntity<Object> getBlogByAccountId(@RequestParam("accountId") UUID accountId) {

        List<BlogResponse> blogResponseList = blogService.getBlogByAccountId(accountId);
        return ResponseBuilder.returnData(HttpStatus.OK, "Get All Blog By AccountID Successfully",
                blogResponseList);
    }

    @Operation(summary = "Get All Blog ", description = "Get All Blog")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllBlog() {
        List<BlogResponse> blog = blogService.getAllBlogs();
        return ResponseBuilder.returnData(HttpStatus.OK,"Get All Blog successfully",
                blog);
    }

    @Operation(summary = "Update Blog By ID", description = "Get Blog By ID")
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateBlog(@RequestParam("id") Long id,
                                             @RequestPart("title") String title,
                                             @RequestPart("content") String content,
                                             @RequestPart("files") List<MultipartFile> files) {

        UpdateBlog updateBlog = new UpdateBlog(title, content);
        blogService.updateBlog(id, updateBlog, files);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Update Blog Successfully");
    }

    @Operation(summary = "Delete Blog", description = "Delete Blog")
    @DeleteMapping
    public ResponseEntity<Object> deleteBlog(@RequestParam("id") Long id
                                             ) {
        // delete blog by change blog status , not delete all information
        blogService.deleteBlog(id);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Delete Blog Successfully");
    }


    @Operation(summary = "Update Blogs status with account: Manager ",
            description = "Update the Blog status By Role. Role required: MANAGER")
    @PutMapping("/approved")
    public ResponseEntity<Object> updateBlogByRole(
            @RequestBody UpdateBlogByManager updateBlogByManager) {

        blogService.updateBlogByManager(updateBlogByManager);
        return ResponseBuilder.returnMessage(
                HttpStatus.OK, "Blog Status approved by Manager successfully");
    }

    @Operation(summary = "Rejected Blog status ", description = "Reject the status of an Blog. Role required: MANAGER")
    @PutMapping("/rejected")
    public ResponseEntity<Object> cancelBlog(@RequestParam("id") Long id) {
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


    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadBlogImg(
            @RequestParam("blogId") Long blogId,
            @RequestParam(value = "files") List<MultipartFile> files) {
        BlogResponse blogImg = blogService.uploadBlogImg(blogId, files);
        return ResponseBuilder.returnData(
                HttpStatus.OK,
                "Images uploaded successfully",
                blogImg
        );
    }

    @DeleteMapping("/images")
    public ResponseEntity<Object> deleteAllImages(@RequestParam("blogId") Long blogId) {
        BlogResponse blogImg = blogService.deleteAllImages(blogId);
        return ResponseBuilder.returnData(HttpStatus.OK, "All images deleted successfully", blogImg);
    }

    @DeleteMapping("/images/by-url")
    public ResponseEntity<Object> deleteImageByUrl(
            @RequestParam("blogId") Long blogId,
            @RequestParam("imageUrl") String imageUrl) {
        BlogResponse blogImg = blogService.deleteImageByUrl(blogId, imageUrl);
        return ResponseBuilder.returnData(HttpStatus.OK, "Image deleted successfully", blogImg);
    }

}
