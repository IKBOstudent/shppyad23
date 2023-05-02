package com.example.shppyad23.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    public Boolean createUser(User user) {
        User userInDb = userRepository.findByUsername(user.getUsername());
        if (userInDb != null) {
            return false;
        }
        String prevPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), prevPassword));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("registered");
        return true;
    }

    public Boolean loginUser(User user) {
        User userInDb = userRepository.findByUsername(user.getUsername());
        if (userInDb == null) {
            return false;
        }

        if (passwordEncoder.matches(user.getPassword(), userInDb.getPassword())) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        }
        return false;

    }

}
