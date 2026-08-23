package io.github.konradwojdyna.bilecik.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @Email
        @NotBlank(message = "Email is required")
        @Size(max = 255, message = "Email is 255 length max")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 255, message = "Password is min 8 and 255 length max")
        String password
) {}
