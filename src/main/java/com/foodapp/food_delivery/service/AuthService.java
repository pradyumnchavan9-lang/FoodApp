package com.foodapp.food_delivery.service;


import com.foodapp.food_delivery.dto.AuthResponse;
import com.foodapp.food_delivery.dto.LoginRequest;
import com.foodapp.food_delivery.dto.RegisterRequest;
import com.foodapp.food_delivery.enums.Role;
import com.foodapp.food_delivery.exception.EmailAlreadyExistsException;
import com.foodapp.food_delivery.exception.UserNotFoundException;
import com.foodapp.food_delivery.model.User;
import com.foodapp.food_delivery.repository.UserRepository;
import com.foodapp.food_delivery.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(request.getEmail()));
        authResponse.setName(user.getName());
        authResponse.setEmail(user.getEmail());
        authResponse.setRole(user.getRole());
        return authResponse;
    }

    public AuthResponse login(LoginRequest request){
        Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtService.generateToken(request.getEmail()));
        authResponse.setName(user.getName());
        authResponse.setEmail(user.getEmail());
        authResponse.setRole(user.getRole());
        return authResponse;
    }


    public User getLoggedInUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
