package com.swp391.hivtmss.model.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.BlogStatus;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponse {
    @JsonProperty(value = "blog_id", index = 1)
    private Long id;

    @JsonProperty(value = "title", index = 2)
    private String title;

    @JsonProperty(value = "content", index = 3)
    private String content;

    @JsonProperty(value = "status", index = 4)
    private BlogStatus status;

    @Column(name = "last_modified_date", nullable = false)
    private Date lastModifiedDate;

    @JsonProperty(value = "created_date", index = 6)
    private Date created_Date;

    @JsonProperty(value = "is_hidden", index = 7)
    private boolean isHidden;

    @JsonProperty(value = "account_id", index = 8)
    private UUID accountId;

    @JsonProperty(value = "full_name", index = 9)
    private String fullName;

    private List<String> imageUrls;

}
