package copypaste.ticketguru.web.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.JwtResponse;
import copypaste.ticketguru.domain.RESTError;
import copypaste.ticketguru.domain.UserRepository;
import copypaste.ticketguru.securingweb.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<AppUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            if (passwordMatches(loginRequest.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername()); // Use username to generate token
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RESTError("Invalid credentials"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new RESTError("User not found"));
        }
    }

    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
