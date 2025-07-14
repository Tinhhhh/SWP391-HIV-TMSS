package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.entity.BlogImg;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    @Schema(description = "Tiêu đề bài viết", required = true)
    private String title;

    @Schema(description = "Nội dung bài viết", required = true)
    @Column(columnDefinition = "TEXT")
    private String content;

    @Schema(description = "ID tài khoản người tạo blog", required = true)
    private UUID accountID;
}
