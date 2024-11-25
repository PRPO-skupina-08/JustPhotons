package si.justphotons.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Table(name = "users")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM user u"),
                @NamedQuery(name = "User.getByUsername",
                        query = "SELECT u FROM user u WHERE u.username = :uporabniskoIme")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<br/>Uporabnisko ime: ");
        sb.append(this.username);
        sb.append("<br/>Email: ");
        sb.append(this.email);
        return sb.toString();
    }
}
