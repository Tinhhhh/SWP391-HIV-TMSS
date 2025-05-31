package com.swp391.hivtmss.model.payload.response;

import com.swp391.hivtmss.model.entity.Account;
import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {
    private Long id;
    private String title;
    private String content;

    private BlogStatus status;

    private String imageUrl;
    private boolean isHidden;

    private UUID accountId;
    private String accountUsername;
}
