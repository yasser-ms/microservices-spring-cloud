package com.parking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRegistrationRequest(
        @NotBlank(message = "First name cannot be empty")
        String firstName,

        @NotBlank(message = "Last name cannot be empty")
        String lastName,

        @Email(message = "Email is not valid")
        @NotBlank(message = "Email cannot be empty")
        String email
) {
    /* SOME CODE IN HERE*/

}
