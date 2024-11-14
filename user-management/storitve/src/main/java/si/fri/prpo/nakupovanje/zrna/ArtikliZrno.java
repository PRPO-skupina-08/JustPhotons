package si.fri.prpo.nakupovanje.zrna;

import si.fri.prpo.nakupovanje.entitete.Artikel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.logging.Logger;

@ApplicationScoped
public class ArtikliZrno {

    private Logger log = Logger.getLogger(ArtikliZrno.class.getName());

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + ArtikliZrno.class.getSimpleName());

        // inicializacija virov
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + ArtikliZrno.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "nakupovalni-seznami-jpa")
    private EntityManager em;

    @Transactional
    public Artikel dodajArtikel(Artikel artikel) {

        // TODO: missing implementation

        return null;

    }

}
