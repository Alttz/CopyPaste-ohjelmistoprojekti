package copypaste.ticketguru.web.rest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
public class AuthController {
    private final String rooturl = "/api/auth2";
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(rooturl)
    public String login(@RequestBody LoginRequest loginRequest) {

        /*
        //https://stackoverflow.com/questions/68114184/can-i-call-spring-security-login-post-method-programmatically
        public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
            try {
                request.login(username, password);
            }
            catch (ServletException e) {
                LOGGER.error("Error while login ", e);
            }
        }
        */




        // Perform authentication
        //if this succeedes we run jwtToken generatio and return it to the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token by passing username to generateToken
        String token = JwtUtil.generateToken("username"); // Replace this with your JWT token generation logic


        
        return token;
    }
}



