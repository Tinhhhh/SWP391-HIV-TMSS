package com.swp391.hivtmss.model.payload.request;

import com.swp391.hivtmss.model.entity.BlogImg;
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


    @NotBlank(message = "Blog title cannot be blank")
    private String title;

    @NotNull(message = "Content cannot be blank")
    private String content;

    private UUID accountID;
}
