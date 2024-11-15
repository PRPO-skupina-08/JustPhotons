package si.justphotons.beans;

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

        User user1 = new User();
        user1.setId(currentId++);
        user1.setUporabniskoIme("Janez");
        user1.setEmail("janez.novak42@gmail.com");
        User user2 = new User();
        user2.setId(currentId++);
        user2.setUporabniskoIme("Marija");
        user2.setEmail("maria.novakus.neki@yahoo.com");
        User user3 = new User();
        user3.setId(currentId++);
        user3.setUporabniskoIme("Kristina");
        user3.setEmail("kristjanska.dusa@amen.com");
        users.add(user1);
        users.add(user2);
        users.add(user3);


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
        return users;
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

        return (long) users.size();

    }

//    public List<User> getUsersCriteriaAPI() {
//
//        // TODO: missing implementation, ko bo baza
//
//        return null;
//    }

    public User getUser(int uporabnikId) {

        User user = null;
        for (User userTmp : users) {
            if (userTmp.getId().equals(uporabnikId)) {
                user = userTmp;
            }
        }
        return user;
    }

    @Transactional
    public User addUser(User user) {

        user.setId(currentId++);
        users.add(user);
        return user;
    }

    @Transactional
    public User updateUser(int userId, User user) {

        // TODO: kako zgleda update? A tkole?
        deleteUser(userId);
        user.setId(userId);
        addUser(user);
        return user;

    }

    @Transactional
    public boolean deleteUser(int uporabnikId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(uporabnikId)) {
                users.remove(i);
                return true;
            }
        }

        return false;

    }
}
