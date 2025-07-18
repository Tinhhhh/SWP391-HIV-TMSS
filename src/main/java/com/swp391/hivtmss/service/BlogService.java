package com.swp391.hivtmss.service;

import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.request.UpdateBlog;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByCustomer;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByManager;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.model.payload.response.ListResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface BlogService {
    void createBlog(@Valid BlogRequest blogRequest, List<MultipartFile> files);

    BlogResponse getBlogById(Long id);

    List<BlogResponse> getBlogByAccountId(UUID accountId);

    List<BlogResponse> getAllBlogs();

    void updateBlog(Long id, UpdateBlog updateBlog, List<MultipartFile> files);

    void deleteBlog(Long id);

    void updateBlogByManager(Long id, UUID accountID, BlogStatus blogStatus);

    void cancelBlog(Long accountId);

    ListResponse getAllBlog(int pageNo, int pageSize, String sortBy, String sortDir, String searchTerm);

    BlogResponse uploadBlogImg(Long blogId, List<MultipartFile> files);

    BlogResponse deleteAllImages(Long blogId);

    BlogResponse deleteImageByUrl(Long blogId, String imageUrl);


}
