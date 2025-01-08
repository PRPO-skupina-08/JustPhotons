package si.justphotons.users.api.v1.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginEssentials {

    @Email(message = "valid email please")
    @Column(name = "email")
    private String email;

    @Size(min = 8, max = 64, message = "password should be between 8 and 64 characters long")
    @Column(name = "password")
    private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
