package hieukientung.booktour.controller;

import hieukientung.booktour.model.Role;
import hieukientung.booktour.model.User;
import hieukientung.booktour.repository.RoleRepository;
import hieukientung.booktour.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping
    public String register(@ModelAttribute("user") @Valid User user, Model model) {
        // check if username already exists
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("usernameExists", true);
            return "register";
        }

        // insert the default role (user) for a new user
//        Role userRole = new Role();
//        userRole.setName("USER");
//        user.setRoles(Collections.singleton(userRole));

        // find role in database depends on "role name" or "role id" already exists
        Optional<Role> existingRole = roleRepository.findByName("USER");

        if(existingRole.isPresent()) {
            // assign the existing role for user account
            user.setRoles(Collections.singleton(existingRole.get()));
        }

        // Encrypt the password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // save the new user's information into database
        userRepository.save(user);

        return "redirect:/login";
    }
}
