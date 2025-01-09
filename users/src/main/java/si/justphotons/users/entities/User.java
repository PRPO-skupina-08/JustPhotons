package si.justphotons.users.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User{
    
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "username")
    private String username;

    @Email(message = "valid email please")
    @Column(name = "email")
    private String email;

    @Size(min = 8, max = 64, message = "password should be between 8 and 64 characters long")
    @Column(name = "password")
    private String password;

    

	public Long getId() {
        return id;
    }

    public void setId(Long id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    // @Override
    // public String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     sb.append("<br/>Id: ");
    //     sb.append(this.id);
    //     sb.append("<br/>Uporabnisko ime: ");
    //     sb.append(this.username);
    //     sb.append("<br/>Email: ");
    //     sb.append(this.email);
    //     return sb.toString();
    // }

    // @Override
    // public User clone() {
    //     User user = new User();
    //     user.setEmail(new StringBuffer(email).toString());
    //     user.setId(id);
    //     user.setUsername(new StringBuffer(username).toString());
    //     return user;
    // }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     User that = (User) o;
    //     return id.equals(that.id) &&
    //     email.equals(that.email) &&
    //     username.equals(that.username);
    // }

}
