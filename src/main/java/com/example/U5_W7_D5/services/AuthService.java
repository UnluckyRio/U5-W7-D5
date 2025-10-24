package com.example.U5_W7_D5.services;

import com.example.U5_W7_D5.dto.AuthResponseDTO;
import com.example.U5_W7_D5.dto.LoginDTO;
import com.example.U5_W7_D5.dto.UserRegistrationDTO;
import com.example.U5_W7_D5.entities.User;
import com.example.U5_W7_D5.repositories.UserRepository;
import com.example.U5_W7_D5.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato con email: " + email));
    }

    public AuthResponseDTO register(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        User user = new User();
        user.setNome(dto.getNome());
        user.setCognome(dto.getCognome());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
    }

    public AuthResponseDTO login(LoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDTO(token, user.getEmail(), user.getRole().name());
    }
}

