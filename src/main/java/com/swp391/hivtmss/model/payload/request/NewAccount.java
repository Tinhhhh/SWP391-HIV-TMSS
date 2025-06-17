package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewAccount {
    @Schema(description = "User's first name", example = "Vo Van")
    @NotEmpty(message = "First name is mandatory")
    @JsonProperty("first_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "First name contain only characters")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Schema(description = "User's last name", example = "Tinh")
    @NotEmpty(message = "Last name is mandatory")
    @JsonProperty("last_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Last name contain only characters")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Schema(description = "User's email address", example = "Java@example.com")
    @NotEmpty(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @Schema(description = "User's address", example = "123 Main St, Springfield")
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @Schema(description = "User's phone number", example = "(+84)794801006")
    @Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;

    @JsonProperty("role_name")
    private RoleName roleName;
}
