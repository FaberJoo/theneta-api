package site.theneta.api.apiRest.v1.member.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
                message = "비밀번호는 8자 이상, 영문, 숫자, 특수문자를 포함해야 합니다.")
        String password,

        @NotBlank(message = "사용자 이름은 필수입니다.")
        @Size(min = 2, max = 20, message = "사용자 이름은 2자 이상 20자 이하여야 합니다.")
        @Pattern(regexp = "^[a-z0-9_]{4,20}$",
                message = "사용자 이름은 영문 소문자, 숫자, 언더스코어만 사용 가능하며 4~20자 이내여야 합니다.")
        String username,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 30, message = "이름은 2자 이상 30자 이하여야 합니다.")
        String name
) { }