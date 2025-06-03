package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.entity.Blog;
import com.swp391.hivtmss.model.entity.DoctorDegree;
import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import com.swp391.hivtmss.model.payload.exception.ResourceNotFoundException;
import com.swp391.hivtmss.model.payload.request.BlogRequest;
import com.swp391.hivtmss.model.payload.response.BlogResponse;
import com.swp391.hivtmss.repository.AccountRepository;
import com.swp391.hivtmss.repository.BlogRepository;
import com.swp391.hivtmss.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public BlogResponse createBlog(BlogRequest blogRequest) {
        Account account = accountRepository.findById(blogRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Blog blog = new Blog();
        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setImageUrl(blogRequest.getImageUrl());
        blog.setStatus(blogRequest.getStatus());
        blog.setCreatedDate(new Date());
        blog.setAccount(account);

        Blog savedBlog = blogRepository.save(blog);
        return convertToResponse(savedBlog);
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));
        return convertToResponse(blog);
    }

    @Override
    public BlogResponse getBlogByAccountId(UUID accountId) {
        Blog blog = blogRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found for this account"));
        return convertToResponse(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogs() {
        List<Blog> degrees = blogRepository.findAll();
        return degrees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BlogResponse updateBlog(Long id, BlogRequest blogRequest) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found"));

        blog.setTitle(blogRequest.getTitle());
        blog.setContent(blogRequest.getContent());
        blog.setImageUrl(blogRequest.getImageUrl());
        blog.setStatus(blogRequest.getStatus());
        blog.setCreatedDate(new Date());

        Blog updatedBlog = blogRepository.save(blog);
        return convertToResponse(updatedBlog);
    }

    @Override
    @Transactional
    public void deleteBlog(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blog not found");
        }
        blogRepository.deleteById(id);
    }

    private BlogResponse convertToResponse(Blog blog) {
        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getContent(),
                blog.getStatus(),
                blog.getImageUrl(),
                blog.getCreatedDate(),
                blog.isHidden(),
                blog.getAccount().getAccountId(),
                blog.getAccount().getEmail()


        );
    }

}
