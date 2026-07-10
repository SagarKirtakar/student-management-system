package com.sagar.sms.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequestDTO {

    @NotBlank(message = "First name is required")
   private String firstName;

    @NotBlank(message = "Last name is required")
   private String lastName;

    @NotNull
    @Email(message = "Invalid email format")
   private String email;


    @Pattern(
            regexp = "^(\\+91 | 91)?[0-9]{10}$",
            message = "phone number must be 10 digits, optional +91 or 91 allowed"

    )
   private String phone;

   @NotNull(message = "Date of birth is required")
   @Past(message = "Date of birth must be in past")
   private LocalDate dateOfBirth;
}
