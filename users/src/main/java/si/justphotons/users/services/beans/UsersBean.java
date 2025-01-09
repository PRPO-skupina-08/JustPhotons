package si.justphotons.users.services.beans;

import java.util.List;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import si.justphotons.users.api.v1.dtos.LoginEssentials;
import si.justphotons.users.api.v1.dtos.RegistrationEssentials;
import si.justphotons.users.api.v1.dtos.UserEssentials;
import si.justphotons.users.entities.User;
import si.justphotons.users.entities.UsersRepository;
import si.justphotons.users.jwt.JwtUtils;

@Service
public class UsersBean {
    UsersRepository usersRepository;
    JwtUtils jwtUtils;

    public UsersBean(UsersRepository u, JwtUtils j) {
        this.usersRepository = u;
        this.jwtUtils = j;
    }

    /* returns JWT */
    @Transactional
    public String register(RegistrationEssentials registrationEssentials) {
        List<User> users = usersRepository.getByEmail(registrationEssentials.getEmail());
        if (users.size() == 0) {
            User user = new User();
            user.setEmail(registrationEssentials.getEmail());
            user.setUsername(registrationEssentials.getUsername());
            user.setPassword(passwordEncoder().encode(registrationEssentials.getPassword()));
            User us = usersRepository.save(user);

            String jwt = jwtUtils.generateToken(us);

            return jwt;
        }
        return null;
    }

    @Transactional
    public String login(LoginEssentials loginEssentials) {
        User user = usersRepository.getByEmail(loginEssentials.getEmail()).get(0);
        if (passwordEncoder().matches(loginEssentials.getPassword(), user.getPassword())) {
            String jwt = jwtUtils.generateToken(user);
            return jwt;
        }
        return null;
    }

    public UserEssentials getOne(Long userId) {
        Optional<User> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            
            return convertUser2Essentials(user);
        }
        return null;
    }

    public String getIdFromToken(HttpServletRequest request) {
        String jwt = this.jwtUtils.getJwtFromHeader(request);
        if (jwt == null) {
            return null;
        }
		String id = this.jwtUtils.getUserNameFromJwtToken(jwt).get("id").toString();
        return id;
    }


    /* Helper functions */

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserEssentials convertUser2Essentials(User u) {
        UserEssentials essentials = new UserEssentials();
        essentials.setId(u.getId());
        essentials.setEmail(u.getEmail());
        essentials.setUsername(u.getUsername());
        return essentials;
    }
}