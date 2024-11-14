package si.fri.prpo.nakupovanje.zrna;

import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.prpo.nakupovanje.entitete.NakupovalniSeznam;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class NakupovalniSeznamiZrno {

    private Logger log = Logger.getLogger(NakupovalniSeznamiZrno.class.getName());

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + NakupovalniSeznamiZrno.class.getSimpleName());

        // inicializacija virov
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + NakupovalniSeznamiZrno.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "nakupovalni-seznami-jpa")
    private EntityManager em;

    public List<NakupovalniSeznam> pridobiNakupovalneSezname() {

        // TODO: missing implementation

        return null;

    }

    public List<NakupovalniSeznam> pridobiNakupovalneSezname(QueryParameters query) {
        
        // TODO: missing implementation

        return null;

    }

    public Long pridobiNakupovalneSeznameCount(QueryParameters query) {
        
        // TODO: missing implementation

        return null;
    }

    public NakupovalniSeznam pridobiNakupovalniSeznam(int nakupovalniSeznamId) {

        // TODO: missing implementation

        return null;

    }

    @Transactional
    public NakupovalniSeznam dodajNakupovalniSeznam(NakupovalniSeznam nakupovalniSeznam) {

        // TODO: missing implementation

        return null;

    }

    @Transactional
    public void posodobiNakupovalniSeznam(int nakupovalniSeznamId, NakupovalniSeznam nakupovalniSeznam) {

        // TODO: missing implementation

    }

    @Transactional
    public boolean odstraniNakupovalniSeznam(int nakupovalniSeznamId) {

        // TODO: missing implementation

        return false;

    }

}
