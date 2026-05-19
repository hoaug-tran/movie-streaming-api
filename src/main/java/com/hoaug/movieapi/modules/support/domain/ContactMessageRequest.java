package com.hoaug.movieapi.modules.support.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactMessageRequest {

  @NotBlank(message = "Vui lòng nhập họ tên")
  @Size(max = 120, message = "Họ tên tối đa 120 ký tự")
  private String name;

  @NotBlank(message = "Vui lòng nhập email")
  @Email(message = "Email không hợp lệ")
  @Size(max = 160, message = "Email tối đa 160 ký tự")
  private String email;

  @NotBlank(message = "Vui lòng chọn chủ đề")
  @Size(max = 32, message = "Mã chủ đề không hợp lệ")
  private String topic;

  @NotBlank(message = "Vui lòng nhập tiêu đề")
  @Size(max = 200, message = "Tiêu đề tối đa 200 ký tự")
  private String subject;

  @NotBlank(message = "Vui lòng nhập nội dung")
  @Size(max = 4000, message = "Nội dung tối đa 4000 ký tự")
  private String message;
}
