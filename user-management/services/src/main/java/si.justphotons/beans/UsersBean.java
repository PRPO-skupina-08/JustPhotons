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
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.justphotons.entities.User;

@ApplicationScoped
public class UsersBean {

    private Logger log = Logger.getLogger(UsersBean.class.getName());
    List<User> users;
    int currentId;

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UsersBean.class.getSimpleName());
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UsersBean.class.getSimpleName());
        // zapiranje virov
    }

    // TODO: to je treba uporabit za povezavo z bazo ...
    @PersistenceContext(unitName = "just-photons-jpa")
    private EntityManager em;

    public List<User> getUsers() {
        Query query = em.createNamedQuery("User.getAll", User.class);
        return query.getResultList();
    }

    // QueryParameters pomaga, da lahko direkt iz URLja daš paramtere v query (oblike offset=n&limit=m)
    // for using filters: https://github.com/kumuluz/kumuluzee-rest
    public List<User> getUsers(QueryParameters query) {
        List<User> users = JPAUtils.queryEntities(em, User.class, query);
        return users;
    }

    public Long getUsersCount(QueryParameters query) {

        Long count;
        if (query == null) {
            count = JPAUtils.queryEntitiesCount(em, User.class);
        } else {
            count = JPAUtils.queryEntitiesCount(em, User.class, query);
        }

        return count;
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
