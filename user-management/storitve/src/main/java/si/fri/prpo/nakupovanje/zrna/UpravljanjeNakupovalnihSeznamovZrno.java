package si.fri.prpo.nakupovanje.zrna;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.logging.Logger;

import si.fri.prpo.nakupovanje.dtos.ArtikelDto;
import si.fri.prpo.nakupovanje.dtos.NakupovalniSeznamDto;
import si.fri.prpo.nakupovanje.entitete.Artikel;
import si.fri.prpo.nakupovanje.entitete.NakupovalniSeznam;

@ApplicationScoped
public class UpravljanjeNakupovalnihSeznamovZrno {

    private Logger log = Logger.getLogger(UpravljanjeNakupovalnihSeznamovZrno.class.getName());

    @Inject
    private UporabnikiZrno uporabnikiZrno;

    @Inject
    private NakupovalniSeznamiZrno nakupovalniSeznamiZrno;

    @Inject
    private ArtikliZrno artikliZrno;

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UpravljanjeNakupovalnihSeznamovZrno.class.getSimpleName());
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UpravljanjeNakupovalnihSeznamovZrno.class.getSimpleName());

        // zapiranje virov
    }

    @Transactional
    //@ValidirajNakupovalniSeznamDto
    public NakupovalniSeznam ustvariNakupovalniSeznam(NakupovalniSeznamDto nakupovalniSeznamDto) {

        // TODO: missing implementation
        
        return null;

    }

    @Transactional
    public Artikel dodajArtikelNaNakupovalniSeznam(int nakupovalniSeznamId, ArtikelDto artikelDto) {

        // TODO: missing implementation
        
        return null;

    }

}
