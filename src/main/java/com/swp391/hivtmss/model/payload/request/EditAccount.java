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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAccount {

    @Schema(description = "Account's first name", example = "Nguyen Thanh")
    @JsonProperty("first_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "First name contain only characters")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Schema(description = "Account's last name", example = "Cong")
    @JsonProperty("last_name")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Last name contain only characters")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Schema(description = "Account's address", example = "123 Main St, Springfield")
    @NotBlank(message = "Address cannot be blank")
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @Schema(description = "Account's gender", example = "Male")
    private Gender gender;

    @Past(message = "Date of birth must be in the past")
    @Schema(description = "Account's date of birth", example = "2003-03-25")
    private LocalDate dob;

    @Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;
}
