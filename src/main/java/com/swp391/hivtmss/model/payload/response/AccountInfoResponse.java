package com.swp391.hivtmss.model.payload.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfoResponse {

    @JsonProperty(value = "account_id", index = 1)
    private UUID accountId;

    @JsonProperty(value = "last_name", index = 2)
    private String lastName;

    @JsonProperty(value = "first_name", index = 3)
    private String firstName;

    @JsonProperty(value = "email", index = 4)
    private String email;

    @JsonProperty(value = "gender", index = 5)
    private String gender;

    @JsonProperty(value = "phone", index = 6)
    private String phone;

    @JsonProperty(value = "avatar", index = 7)
    private String avatar;

    @JsonProperty(value = "address", index = 8)
    private String address;

    @JsonProperty(value = "dob", index = 9)
    private String dob;

    @JsonProperty(value = "role_id", index = 14)
    private Long roleId;

    @JsonProperty(value = "role_name", index = 15)
    private String roleName;

}

