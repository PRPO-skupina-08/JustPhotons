package si.fri.prpo.nakupovanje.servleti;

//import si.fri.prpo.nakupovanje.dtos.NakupovalniSeznamDto;
//import si.fri.prpo.nakupovanje.entitete.NakupovalniSeznam;
import si.fri.prpo.nakupovanje.zrna.UporabnikiZrno;
import si.fri.prpo.nakupovanje.zrna.UpravljanjeNakupovalnihSeznamovZrno;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

@WebServlet("/servlet")
public class JPAServlet extends HttpServlet {

    @Inject
    private UporabnikiZrno uporabnikiZrno;

    @Inject
    private UpravljanjeNakupovalnihSeznamovZrno upravljanjeNakupovalnihSeznamovZrno;

    private static final Logger log = Logger.getLogger(JPAServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter writer = resp.getWriter();

        // izpis uporabnikov
        writer.append("<br/><br/>Uporabniki:<br/>");
        writer.append("Bo treba pa še kaj narediti preden se kaj  izpiše tu :))" + "<br/><br/>");

        // TODO: missing implementation
        
    }
}
