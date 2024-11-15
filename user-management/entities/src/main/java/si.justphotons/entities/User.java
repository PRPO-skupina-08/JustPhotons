package si.justphotons.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uporabnik")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM Uporabnik u"),
                @NamedQuery(name = "User.getByUporabniskoIme",
                        query = "SELECT u FROM Uporabnik u WHERE u.uporabniskoIme = :uporabniskoIme")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uporabnisko_ime")
    private String uporabniskoIme;

    private String email;

    @JsonbTransient
    @OneToMany(mappedBy = "uporabnik", cascade = CascadeType.ALL)

    public Integer getId() {
        return id;
    }

    // TODO: Tega pol ne bo tle več
    public void setId(Integer id) {
        this.id = id;
    }

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
