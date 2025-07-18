package com.example.demo.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContactDTO {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
	private String name;
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?\\d{7,}$", message = "Invalid phone")
	private String phone;
	
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
	private String email;
	
	public ContactDTO(String name, String phone, String email) {
		this.email = email;
		this.name = name;
		this.phone = phone;
	}

	public ContactDTO() {
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
