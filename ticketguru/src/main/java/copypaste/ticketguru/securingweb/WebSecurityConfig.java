package copypaste.ticketguru.securingweb;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        		.cors(withDefaults())
                .csrf(csrf -> csrf.disable()) // Disable CSRF
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/**").permitAll()
                                .anyRequest().authenticated()
                )
                
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                    )

                .logout(logout -> logout.permitAll());
        return http.build();
    }
	
	@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
        		"http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:4000",
                "http://localhost:5000",
		"http://localhost:5173",
                "http://localhost:8000",
                "http://localhost:8080", 
                "http://127.0.0.1:3000",
                "http://127.0.0.1:4200",
                "http://127.0.0.1:4000",
                "http://127.0.0.1:5000",
		"http://127.0.0.1:5173",
                "http://127.0.0.1:8000",
                "http://127.0.0.1:8080",
        		"https://projekti-ticketguru-tiimi4.rahtiapp.fi")); // change origin accordingly
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); 
        configuration.setAllowCredentials(true); 
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration); 
        return source;
    }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
