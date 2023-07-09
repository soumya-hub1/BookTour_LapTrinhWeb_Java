package hieukientung.booktour.controller;

import hieukientung.booktour.model.Role;
import hieukientung.booktour.model.User;
import hieukientung.booktour.repository.RoleRepository;
import hieukientung.booktour.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private AccountService accountServiceService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = accountServiceService.getByUsername(username);
        model.addAttribute("user", user);
        return "/update-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("admin") @Valid User user,
                                @RequestParam("oldImage") String oldImage,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Optional<Role> existingRole = roleRepository.findByName("USER");
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path path = Paths.get("target/classes/static/assets/images/user/" + fileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            user.setImage("/assets/images/user/" + fileName);
            if (existingRole.isPresent()) {
                user.setRoles(Collections.singleton(existingRole.get()));
            }
        } else {
            user.setImage(oldImage);
            if (existingRole.isPresent()) {
                user.setRoles(Collections.singleton(existingRole.get()));
            }
        }
        accountServiceService.saveUser(user);
        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        String username = principal.getName();
        User user = accountServiceService.getByUsername(username);
        model.addAttribute("user", user);
        return "/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal) {
        String username = principal.getName();
        User user = accountServiceService.getByUsername(username);
        if (newPassword.equals(confirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            accountServiceService.saveUser(user);
        }
        return "redirect:/change-password";
    }
}
