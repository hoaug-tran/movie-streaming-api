package com.hoaug.movieapi.modules.auth.application.dto.request;

import com.hoaug.movieapi.common.validator.ValidSafeString;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
  @NotBlank
  @Size(min = 6, max = 50)
  @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Username can only contain letters, numbers, underscore and hyphen")
  private String username;

  @NotBlank
  @Email(message = "Email không đúng định dạng.")
  @Size(max = 100)
  private String email;

  @NotBlank
  @Size(min = 8, max = 255)
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt.")
  private String password;

  @NotBlank
  @Size(max = 100)
  @ValidSafeString
  private String fullName;

  @Size(max = 500)
  private String avatarUrl;

  public String getUsername () {
    return username;
  }

  public void setUsername (String username) {
    this.username = username;
  }

  public String getEmail () {
    return email;
  }

  public void setEmail (String email) {
    this.email = email;
  }

  public String getPassword () {
    return password;
  }

  public void setPassword (String password) {
    this.password = password;
  }

  public String getFullName () {
    return fullName;
  }

  public void setFullName (String fullName) {
    this.fullName = fullName;
  }

  public String getAvatarUrl () {
    return avatarUrl;
  }

  public void setAvatarUrl (String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  

}
