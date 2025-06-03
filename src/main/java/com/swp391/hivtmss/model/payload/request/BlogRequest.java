package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import com.swp391.hivtmss.model.payload.enums.Classification;
import com.swp391.hivtmss.model.payload.enums.StudyMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @NotNull(message = "Status ate cannot be null")
    private BlogStatus status;

    @NotNull(message = "ImageUrl cannot be null")
    private String imageUrl;

    @NotNull(message = "Account ID cannot be null")
    private UUID accountId;

    @NotNull(message = "Hidden cannot be null")
    private Boolean isHidden;







}
