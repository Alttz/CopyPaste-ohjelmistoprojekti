package copypaste.ticketguru.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import copypaste.ticketguru.securingweb.JwtUtil;

@Service
public class JwtValidatorService {
    @Autowired
    private JwtUtil jwtUtil;

    public Boolean validateToken(String authHeader) {
        if(authHeader == null) {
            return false;
        }

        if(!authHeader.startsWith("Bearer ")) {
            return false;
        }

        return jwtUtil.validateToken(authHeader.substring(7));
    }
}
