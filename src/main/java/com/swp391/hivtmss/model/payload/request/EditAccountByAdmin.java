package com.swp391.hivtmss.model.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swp391.hivtmss.model.payload.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditAccountByAdmin {

    @JsonProperty(value = "last_name", index = 1)
    @Pattern(regexp = "^[a-zA-Z]*$", message = "Last name contain only characters")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @JsonProperty(value = "first_name", index = 2)
    @Pattern(regexp = "^[a-z A-Z]*$", message = "First name contain only characters")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @JsonProperty(index = 3)
    private Gender gender;

    @JsonProperty(index = 4)
    @Pattern(regexp = "(84|0[3|5|7|8|9])([0-9]{8})\\b", message = "Please enter a valid(+84) phone number")
    private String phone;

    @JsonProperty(index = 5)
    @Size(max = 100, message = "Address must be less than 100 characters")
    private String address;

    @JsonProperty(index = 6)
    @Past(message = "Date of birth must be in the past")
    @Schema(description = "Account's date of birth", example = "2003-03-25")
    private LocalDate dob;

    @JsonProperty(value = "is_locked", index = 7)
    private boolean isLocked;

    @JsonProperty(value = "role_id", index = 8)
    private Long roleId;

    @JsonProperty(value = "introduction", index = 9)
    private String introduction;
}
