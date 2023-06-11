package hieukientung.booktour.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {

    private UserDetailsService userDetailsService;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/admin").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/create-tour").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/update-tour/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/save").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/delete-tour/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/view-list-tours").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/view-detail-tour/**").hasAnyAuthority("ADMIN")
                                .requestMatchers("/admin/view-list-tours/page/**").hasAnyAuthority("ADMIN")
                                .anyRequest().authenticated()

                ).formLogin(
                        form -> form
                                .loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl("/")
                                .permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                ).exceptionHandling(
                        exception -> exception
                                .accessDeniedPage("/access-denied")
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
