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
                @NamedQuery(name = "User.getByUporabniskoIme",
                        query = "SELECT u FROM user u WHERE u.uporabniskoIme = :uporabniskoIme")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String uporabniskoIme;

    @Column(name = "email")
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getUporabniskoIme() {
        return uporabniskoIme;
    }

    public void setUporabniskoIme(String uporabniskoIme) {
        this.uporabniskoIme = uporabniskoIme;
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
        sb.append(this.uporabniskoIme);
        sb.append("<br/>Email: ");
        sb.append(this.email);
        return sb.toString();
    }
}
