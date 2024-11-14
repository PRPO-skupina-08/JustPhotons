package si.fri.prpo.nakupovanje.entitete;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "artikel")
@NamedQueries(value =
        {
                @NamedQuery(name = "Artiel.getAll", query = "SELECT a FROM Artikel a")
        })
public class Artikel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "naziv")
    private String naziv;

    @Column(name = "opis")
    private String opis;

    @JsonbTransient
    @ManyToOne
    @JoinColumn(name = "nakupovalni_seznam_id")
    private NakupovalniSeznam nakupovalniSeznam;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public NakupovalniSeznam getNakupovalniSeznam() {
        return nakupovalniSeznam;
    }

    public void setNakupovalniSeznam(NakupovalniSeznam nakupovalniSeznam) {
        this.nakupovalniSeznam = nakupovalniSeznam;
    }
}
