package si.fri.prpo.nakupovanje.entitete;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uporabnik")
@NamedQueries(value =
        {
                @NamedQuery(name = "Uporabnik.getAll", query = "SELECT u FROM Uporabnik u"),
                @NamedQuery(name = "Uporabnik.getByUporabniskoIme",
                        query = "SELECT u FROM Uporabnik u WHERE u.uporabniskoIme = :uporabniskoIme")
        })
public class Uporabnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ime;

    private String priimek;

    @Column(name = "uporabnisko_ime")
    private String uporabniskoIme;

    private String email;

    @JsonbTransient
    @OneToMany(mappedBy = "uporabnik", cascade = CascadeType.ALL)
    private List<NakupovalniSeznam> nakupovalniSeznami;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPriimek() {
        return priimek;
    }

    public void setPriimek(String priimek) {
        this.priimek = priimek;
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

    public List<NakupovalniSeznam> getNakupovalniSeznami() {

        if (nakupovalniSeznami == null) {
            nakupovalniSeznami = new ArrayList<>();
        }

        return nakupovalniSeznami;
    }

    public void setNakupovalniSeznami(List<NakupovalniSeznam> nakupovalniSeznami) {
        this.nakupovalniSeznami = nakupovalniSeznami;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ime: ");
        sb.append(this.ime);
        sb.append("<br/>Priimek: ");
        sb.append(this.priimek);
        sb.append("<br/>Uporabnisko ime: ");
        sb.append(this.uporabniskoIme);
        sb.append("<br/>Email: ");
        sb.append(this.email);
        return sb.toString();
    }
}
