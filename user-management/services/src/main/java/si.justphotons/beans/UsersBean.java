package si.justphotons.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.NotFoundException;


import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;

import si.justphotons.entities.User;

@ApplicationScoped
public class UsersBean {

    private Logger log = Logger.getLogger(UsersBean.class.getName());
    List<User> users;
    int currentId;

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UsersBean.class.getSimpleName());

        users = new ArrayList<User>();
        currentId = 0;

//        User user1 = new User();
//        user1.setUporabniskoIme("Janez");
//        user1.setEmail("janez.novak42@gmail.com");
//        User user2 = new User();
//        user2.setUporabniskoIme("Marija");
//        user2.setEmail("maria.novakus.neki@yahoo.com");
//        User user3 = new User();
//        user3.setUporabniskoIme("Kristina");
//        user3.setEmail("kristjanska.dusa@amen.com");
//
//        em.persist(user1);
//        em.persist(user2);
//        em.persist(user3);
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UsersBean.class.getSimpleName());

        // TODO: a je treba tukaj dealocirat ArrayList?
        // zapiranje virov
    }

    // TODO: to je treba uporabit za povezavo z bazo ...
    @PersistenceContext(unitName = "just-photons-jpa")
    private EntityManager em;

    public List<User> getUsers() {
        Query query = em.createNamedQuery("User.getAll", User.class);
        return query.getResultList();
    }

    // QueryParameters pomaga, da lahko direkt iz URLja daš paramtere v query
//    public List<User> getUsers(QueryParameters query) {
//        TODO: ko bo baza
//        return null;
//
//    }

    public Long getUsersCount(QueryParameters query) {
        
        // TODO: query shoudn't be null -
        // -> querying count with parameters

        return (long) 42;

    }

//    public List<User> getUsersCriteriaAPI() {
//
//        // TODO: missing implementation, ko bo baza
//
//        return null;
//    }

    public User getUser(int uporabnikId) {

        User user = em.find(User.class, uporabnikId);
        if (user == null) {
            throw new NotFoundException();
        }
        return user;
    }

    @Transactional
    public User addUser(User user) {

        em.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(int userId, User user) {

        User u = em.find(User.class, userId);
        if (u == null) {
            return null;
        }
        user.setId(u.getId());
        user = em.merge(user);

        return user;

    }

    @Transactional
    public boolean deleteUser(int userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            em.remove(u);
        } else return false;

        return true;
    }
}
