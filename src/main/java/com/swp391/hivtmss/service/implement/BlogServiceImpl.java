package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Blog;
import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import com.swp391.hivtmss.model.payload.exception.HivtmssException;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.request.UpdateBlog;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByCustomer;
import com.swp391.hivtmss.model.payload.request.UpdateBlogByManager;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.BlogRepository;
import com.swp391.hivtmss.service.BlogService;
import lombok.RequiredArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper restrictedModelMapper;

    @Override
    @Transactional
    public void createBlog(BlogRequest blogRequest) {
        Account account = accountRepository.findById(blogRequest.getAccountID())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Blog blog = new Blog();

        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setImageUrl(blogRequest.getImageUrl());
        blog.setStatus(BlogStatus.PENDING);
        blog.setCreatedDate(new Date());
        blog.setLastModifiedDate(new Date());
        blog.setHidden(true);
        blog.setAccount(account);

        Blog savedBlog = blogRepository.save(blog);
        convertToResponse(savedBlog);
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        return convertToResponse(blog);
    }

    @Override
    public List<BlogResponse> getBlogByAccountId(UUID accountId) {
        List<Blog> blogs = blogRepository.findByAccountId(accountId)
                ;
        return blogs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<Blog> blogs = blogRepository.findAll();
        return blogs.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBlog(Long id, UpdateBlog updateBlog) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BlogID not found"));

        blog.setTitle(updateBlog.getTitle());
        blog.setContent(updateBlog.getContent());
        blog.setImageUrl(updateBlog.getImageUrl());
        blog.setCreatedDate(new Date());

        blogRepository.save(blog);

    }

    @Override
    @Transactional
    public void deleteBlog(Long id, UpdateBlogByCustomer updateBlogByCustomer) {

        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        // set giá trị isHidden thành true, ko xóa chỉ ẩn Blog đi.
        blog.setHidden(false);
        blogRepository.save(blog);
    }

    @Override
    public void updateBlogByManager(UpdateBlogByManager updateBlogByManager) {
        Account account = accountRepository.findById(updateBlogByManager.getAccountId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, Account not found"));

        Blog blog = blogRepository.findById(updateBlogByManager.getId())
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, blog not found"));
        restrictedModelMapper.map(updateBlogByManager, account);

        blog.setStatus(BlogStatus.APPROVED);
        blogRepository.save(blog);


    }

    @Override
    public void cancelBlog(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, blog not found"));


        if (blog.getStatus() != BlogStatus.PENDING){
            throw new HivtmssException(HttpStatus.BAD_REQUEST, "Request fails, blog is not in pending status");
        }

        blog.setStatus(BlogStatus.REJECTED);
        blogRepository.save(blog);
    }

    private BlogResponse convertToResponse(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getStatus(),
                blog.getImageUrl(),
                blog.getCreatedDate(),
                blog.getLastModifiedDate(),
                blog.isHidden(),
                blog.getAccount().getId(),
                blog.getAccount().fullName()

        );
    }

}
