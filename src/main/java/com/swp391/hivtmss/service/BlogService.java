package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    BlogResponse createBlog(@Valid BlogRequest blogRequest);

    BlogResponse getBlogById(Long id);

    BlogResponse getBlogByAccountId(UUID accountId);

    List<BlogResponse> getAllBlogs();

    BlogResponse updateBlog(Long id, @Valid BlogRequest blogRequest);

    void deleteBlog(Long id);
}
