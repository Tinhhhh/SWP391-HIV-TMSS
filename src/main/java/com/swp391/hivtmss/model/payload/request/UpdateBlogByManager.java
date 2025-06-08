package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogByManager {
    @NotBlank(message = "Blog title cannot be blank")
    private String title;

    @NotNull(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Status ate cannot be null")
    private BlogStatus status;

    @NotNull(message = "ImageUrl cannot be null")
    private String imageUrl;

    @NotNull(message = "Hidden cannot be null")
    private Boolean isHidden;

}
