package io.github.konradwojdyna.bilecik.service;


import io.github.konradwojdyna.bilecik.dto.request.AuthRequest;
import io.github.konradwojdyna.bilecik.dto.response.AuthResponse;
import io.github.konradwojdyna.bilecik.entity.User;
import io.github.konradwojdyna.bilecik.exception.EmailAlreadyExistsException;
import io.github.konradwojdyna.bilecik.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthResponse register(AuthRequest request){

      String normalizedEmail = request.email().toLowerCase(Locale.ROOT).trim();

      if(userRepository.findByEmail(normalizedEmail).isPresent()){
          throw new EmailAlreadyExistsException(normalizedEmail);
      }

       User newUser = new User();

       String hashPassword =  passwordEncoder.encode(request.password());

       newUser.setEmail(normalizedEmail);
       newUser.setPasswordHash(hashPassword);

       User savedUser = userRepository.save(newUser);

       return new AuthResponse(savedUser.getEmail());
    }
}
