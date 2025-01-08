package si.justphotons.users.services.beans;

import java.util.List;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import si.justphotons.users.api.v1.dtos.RegistrationEssentials;
import si.justphotons.users.entities.User;
import si.justphotons.users.entities.UsersRepository;

@Service
public class UsersBean {
    UsersRepository usersRepository;

    public UsersBean(UsersRepository u) {
        this.usersRepository = u;
    }

    @Transactional
    public User register(RegistrationEssentials registrationEssentials) {
        List<User> users = usersRepository.getByEmail(registrationEssentials.getEmail());
        if (users.size() == 0) {
            User user = new User();
            user.setEmail(registrationEssentials.getEmail());
            user.setUsername(registrationEssentials.getUsername());
            user.setPassword(passwordEncoder().encode(registrationEssentials.getPassword()));
            User al = usersRepository.save(user);
            return al;
        }
        return null;
    }


    // public User login(Long orgId, Album album) {
    //     Optional<Organisation> orgOptional = organisationsRepository.findById(orgId);
    //     if (orgOptional.isPresent()) {
    //         Organisation org = orgOptional.get();
    //         album.setOrganisation(org);
    //         Album al = albumsRepository.save(album);
    //         return al;
    //     }
    //     return null;
    // }

    public User getOne(Long userId) {
        Optional<User> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user;
        }
        return null;
    }


    /* Helper functions */

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}