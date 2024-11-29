package si.justphotons.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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
import si.justphotons.servicedtos.UserSendDTO;

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

    @PersistenceContext(unitName = "just-photons-jpa")
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<UserSendDTO> getUsers() {
        Query query = em.createNamedQuery("User.getAll", User.class);
        List<User> users = query.getResultList();
        List<UserSendDTO> dtos = new ArrayList<>();  
        for (User user : users) {
            dtos.add(convertToDTO(user));
        }
        return dtos;
    }

    // QueryParameters pomaga, da lahko direkt iz URLja daš paramtere v query (oblike offset=n&limit=m)
    // for using filters: https://github.com/kumuluz/kumuluzee-rest - ?filter crashes our app!!!
    public List<UserSendDTO> getUsers(QueryParameters query) {
        List<User> users = JPAUtils.queryEntities(em, User.class, query);
        List<UserSendDTO> dtos = new ArrayList<>();  
        for (User user : users) {
            dtos.add(convertToDTO(user));
        }

        return dtos;
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

    public UserSendDTO getUser(int uporabnikId) {

        User user = em.find(User.class, uporabnikId);
        if (user == null) {
            throw new NotFoundException();
        }

        UserSendDTO dto = convertToDTO(user);
        return dto;
    }

    @Transactional
    public UserSendDTO addUser(User user) {

        em.persist(user);
        return convertToDTO(user);
    }

    @Transactional
    public UserSendDTO updateUser(int userId, User user) {

        User u = em.find(User.class, userId);
        if (u == null) {
            throw new NotFoundException();
        }
        user.setId(u.getId());
        User newUser = em.merge(user);

        return convertToDTO(newUser);
    }

    @Transactional
    public boolean deleteUser(int userId) {
        User u = em.find(User.class, userId);
        if (u != null) {
            em.remove(u);
        } else return false;

        return true;
    }

    /*
        DTO conversion methods:
    */

    public User convertToEntity(UserSendDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setId(dto.getId());
        return user;
    }
    public UserSendDTO convertToDTO(User user) {
        UserSendDTO dto = new UserSendDTO();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setId(user.getId());
        return dto;
    }
}
