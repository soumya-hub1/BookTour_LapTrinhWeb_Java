package hieukientung.booktour.controller;

import hieukientung.booktour.model.Role;
import hieukientung.booktour.model.User;
import hieukientung.booktour.repository.RoleRepository;
import hieukientung.booktour.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    public String register(@ModelAttribute("user") @Valid User user,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           Model model) throws IOException {
        if (userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("usernameExists", true);
            return "register";
        }
        Optional<Role> existingRole = roleRepository.findByName("USER");
        if (existingRole.isPresent()) {
            user.setRoles(Collections.singleton(existingRole.get()));
        }
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path path = Paths.get("target/classes/static/assets/images/user/" + fileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/assets/images/user/" + fileName);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/login";
    }
}
