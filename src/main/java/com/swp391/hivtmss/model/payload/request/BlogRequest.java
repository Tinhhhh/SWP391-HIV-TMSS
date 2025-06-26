package com.swp391.hivtmss.model.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {


    @NotBlank(message = "Blog title cannot be blank")
    private String title;

    @NotNull(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "ImageUrl cannot be null")
    private String imageUrl;

    private UUID accountID;
}
