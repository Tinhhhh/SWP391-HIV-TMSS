package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.request.UpdateBlog;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByCustomer;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByManager;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    void createBlog(@Valid BlogRequest blogRequest);

    BlogResponse getBlogById(Long id);

    List<BlogResponse> getBlogByAccountId(UUID accountId);

    List<BlogResponse> getAllBlogs();

    void updateBlog(Long id ,UpdateBlog updateBlog);

    void deleteBlog(Long id, UpdateBlogByCustomer updateBlogByCustomer);

    void updateBlogByManager(UpdateBlogByManager updateBlogByManager);

    void cancelBlog(Long accountId);
}
