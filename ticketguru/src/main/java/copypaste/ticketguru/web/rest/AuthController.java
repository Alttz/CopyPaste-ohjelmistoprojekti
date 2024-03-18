package copypaste.ticketguru.web.rest;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import copypaste.ticketguru.domain.AppUser;
import copypaste.ticketguru.domain.JwtResponse;
import copypaste.ticketguru.domain.UserRepository;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<AppUser> userOptional = userRepository.findByUsername(loginRequest.getUsername());
        
        if (userOptional.isPresent()) {
        	AppUser user = userOptional.get();
            if (passwordMatches(loginRequest.getPassword(), user.getPassword())) {
                String token = generateJwtToken(user);
                return ResponseEntity.ok(new JwtResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
    }


    private boolean passwordMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateJwtToken(AppUser user) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

     // Use java-jwt library methods directly to specify the signing key
        Algorithm algorithm = Algorithm.HMAC256("secret");

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withIssuer("your_name")
                // Add more claims as needed
                .sign(algorithm);
    }

}
