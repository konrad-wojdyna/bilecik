package io.github.konradwojdyna.bilecik.service;


import io.github.konradwojdyna.bilecik.dto.request.AuthRequest;
import io.github.konradwojdyna.bilecik.dto.response.AuthResponse;
import io.github.konradwojdyna.bilecik.entity.User;
import io.github.konradwojdyna.bilecik.exception.EmailAlreadyExistsException;
import io.github.konradwojdyna.bilecik.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public AuthResponse register(AuthRequest request){

      String normalizedEmail = request.email().toLowerCase(Locale.ROOT).trim();

      if(userRepository.findByEmail(normalizedEmail).isPresent()){
          throw new EmailAlreadyExistsException(normalizedEmail);
      }

       User newUser = new User();

       newUser.setEmail(normalizedEmail);
       newUser.setPasswordHash(request.password());

       User savedUser = userRepository.save(newUser);

       return new AuthResponse(savedUser.getEmail());
    }
}
