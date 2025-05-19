package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditAccountByAdmin {

    @JsonProperty("first_name")
    @Pattern(regexp = "^[^0-9]*$", message = "first name must not contain numbers")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @JsonProperty("last_name")
    @Pattern(regexp = "^[^0-9]*$", message = "last name must not contain numbers")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    @Schema(description = "Account's date of birth", example = "2003-03-25")
    private LocalDate dob;

    @Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;

    @JsonProperty("is_locked")
    private boolean isLocked;

    @JsonProperty("role_id")
    private Long roleId;
}
