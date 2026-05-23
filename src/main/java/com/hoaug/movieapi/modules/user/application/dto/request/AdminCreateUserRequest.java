package com.hoaug.movieapi.modules.user.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;
import com.hoaug.movieapi.modules.user.domain.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AdminCreateUserRequest {

    @NotBlank(message = "Vui lòng nhập tên đăng nhập.")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3 đến 50 ký tự.")
    @ValidSafeString(minLength = 3, maxLength = 50)
    private String username;

    @NotBlank(message = "Vui lòng nhập email.")
    @Email(message = "Email không đúng định dạng.")
    @Size(max = 100, message = "Email tối đa 100 ký tự.")
    private String email;

    @NotBlank(message = "Vui lòng nhập mật khẩu.")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự.")
    private String password;

    @NotBlank(message = "Vui lòng nhập họ tên.")
    @Size(min = 1, max = 100, message = "Họ tên phải từ 1 đến 100 ký tự.")
    @ValidSafeString(minLength = 1, maxLength = 100)
    private String fullName;

    @Size(max = 500, message = "Đường dẫn ảnh đại diện tối đa 500 ký tự.")
    private String avatarUrl;

    private Role role;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
