package si.justphotons.dtos;

import javax.validation.constraints.*; // For common validation constraints like @NotNull, @Size, etc.

public class UserCreateDTO {

    @NotNull
    @Size(min = 1, max = 42, message = "username should be from 1 to 42 caracters long")
    private String username;

    @Email(message = "email address must be valid")
    private String email;

    public UserCreateDTO() {
    }

    public String getUsername() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
