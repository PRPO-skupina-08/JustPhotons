package si.fri.prpo.nakupovanje.zrna;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.prpo.nakupovanje.entitete.Uporabnik;

@ApplicationScoped
public class UporabnikiZrno {

    private Logger log = Logger.getLogger(UporabnikiZrno.class.getName());
    List<Uporabnik> users;

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UporabnikiZrno.class.getSimpleName());
        users = new ArrayList<Uporabnik>();
        Uporabnik user1 = new Uporabnik();
        user1.setId(0);
        user1.setIme("Janez");
        user1.setPriimek("Novak");
        user1.setEmail("janez.novak42@gmail.com");
        Uporabnik user2 = new Uporabnik();
        user2.setId(1);
        user2.setIme("Marija");
        user2.setPriimek("Novak");
        user2.setEmail("maria.novakus.neki@yahoo.com");
        Uporabnik user3 = new Uporabnik();
        user3.setId(2);
        user3.setIme("Kristina");
        user3.setPriimek("Lorenzutti");
        user3.setEmail("kristjanska.dusa@amen.com");
        users.add(user1);
        users.add(user2);
        users.add(user3);
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UporabnikiZrno.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "nakupovalni-seznami-jpa")
    private EntityManager em;

    public List<Uporabnik> pridobiUporabnike() {
        return users;
    }

    public List<Uporabnik> pridobiUporabnike(QueryParameters query) {

        return null;

    }

    public Long pridobiUporabnikeCount(QueryParameters query) {
        
        // query shoudn't be null

        return (long) users.size();

    }

    public List<Uporabnik> pridobiUporabnikeCriteriaAPI() {

        // TODO: missing implementation

        return null;
    }

    public Uporabnik pridobiUporabnika(int uporabnikId) {

        Uporabnik user = null;
        for (Uporabnik uporabnik : users) {
            if (uporabnik.getId().equals(uporabnikId)) {
                user = uporabnik;
            }
        }

        return user;

    }

    @Transactional
    public Uporabnik dodajUporabnika(Uporabnik uporabnik) {

        // TODO: better id setting ... (current fails on deletes)
        uporabnik.setId(users.size());
        users.add(uporabnik);

        return uporabnik;

    }

    @Transactional
    public Uporabnik posodobiUporabnika(int uporabnikId, Uporabnik uporabnik) {

        // TODO: missing implementation

        return null;

    }

    @Transactional
    public boolean odstraniUporanbika(int uporabnikId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(uporabnikId)) {
                users.remove(i);
                return true;
            }
        }

        return false;

    }
}
