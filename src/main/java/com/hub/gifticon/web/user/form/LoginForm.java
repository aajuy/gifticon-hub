package com.hub.gifticon.web.user.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginForm {
    @NotBlank
    @Size(min = 1, max = 10)
    private String userId;

    @NotBlank
    @Size(min = 1, max = 10)
    private String password;
}
