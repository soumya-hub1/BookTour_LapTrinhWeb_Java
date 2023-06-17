package hieukientung.booktour.controller;

import hieukientung.booktour.model.Email;
import hieukientung.booktour.model.Role;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.model.User;
import hieukientung.booktour.repository.RoleRepository;
import hieukientung.booktour.service.AccountService;
import hieukientung.booktour.service.AdminService;
import hieukientung.booktour.service.EmailService;
import hieukientung.booktour.service.TourService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private TourService tourService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = {"", "/view-list-tours"})
    public String getAllTours(Model model) {
        return findPaginated(1, model);
    }

    @PostMapping("/save")
    public String saveTour(@ModelAttribute("tour") @Valid Tour tour,
                           @RequestParam("oldImage") String oldImage,
                           @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path path = Paths.get("target/classes/static/assets/images/detail-tour/" + fileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            tour.setImage("/assets/images/detail-tour/" + fileName);
        } else {
            tour.setImage(oldImage);
        }
        tourService.saveTour(tour);
        return "redirect:/admin";
    }

    @GetMapping("/view-detail-tour/{id}")
    public String getTourById(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        if (tour == null) {
            return "/error/404-pagenotfound";
        }
        model.addAttribute("tour", tour);
        return "admin/view-detail-tour";
    }

    @GetMapping("/create-tour")
    public String createTour(Model model) {
        model.addAttribute("listDestination", tourService.getAllDestinationPoint());
        model.addAttribute("listDeparture", tourService.getAllDeparturePoint());
        model.addAttribute("listTourType", adminService.getAllTourType());
        model.addAttribute("listDiscount", adminService.getAllDiscount());
        Tour tour = new Tour();
        model.addAttribute("tour", tour);
        return "admin/create-tour";
    }

    @GetMapping("/update-tour/{id}")
    public String updateTour(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("listDestination", tourService.getAllDestinationPoint());
        model.addAttribute("listDeparture", tourService.getAllDeparturePoint());
        model.addAttribute("listTourType", adminService.getAllTourType());
        model.addAttribute("listDiscount", adminService.getAllDiscount());
        model.addAttribute("tour", tour);
        return "admin/update-tour";
    }

    @GetMapping("/delete-tour/{id}")
    public String deleteTour(@PathVariable("id") Long id) {
        tourService.deleteTour(id);
        return "redirect:/admin/view-list-tours";
    }

    @GetMapping(value = "view-list-tours/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 6;
        Page<Tour> page = tourService.findPaginated(pageNo, pageSize);
        List<Tour> listTours = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        int startPage = Math.max(1, pageNo - 2);
        int endPage = Math.min(startPage + 4, page.getTotalPages());

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("listTours", listTours);
        return "admin/view-list-tours";
    }

    @GetMapping("/send-mail")
    public String showSendMail() {

        return "admin/send-mail";
    }

    @PostMapping("/send-mail")
    public String submitSendMail(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String tourName = request.getParameter("tourName");
        String tourDescription = request.getParameter("tourDescription");

        List<Email> emails = emailService.getAllEmails();
        System.out.println(emails);

        for (Email email : emails) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            String subject = "Tour name:" + tourName;
            String content = "<p><b>Tour Description</b>:" + tourDescription + "<p>";

            helper.setFrom("toutnest2425@gmail.com", "TourNest");
            helper.setTo(email.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
        }

        return "/admin/send-mail-message";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        String username = principal.getName();
        User admin = accountService.getByUsername(username);
        model.addAttribute("admin", admin);
        return "admin/update-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute("admin") @Valid User admin,
                                @RequestParam("oldImage") String oldImage,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Optional<Role> existingRole = roleRepository.findByName("ADMIN");
        if (!imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            Path path = Paths.get("target/classes/static/assets/images/admin/" + fileName);
            Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            admin.setImage("/assets/images/admin/" + fileName);
            if (existingRole.isPresent()) {
                admin.setRoles(Collections.singleton(existingRole.get()));
            }
        } else {
            admin.setImage(oldImage);
            if (existingRole.isPresent()) {
                admin.setRoles(Collections.singleton(existingRole.get()));
            }
        }
        accountService.saveUser(admin);
        return "redirect:/admin/profile";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model, Principal principal) {
        String username = principal.getName();
        User admin = accountService.getByUsername(username);
        model.addAttribute("admin", admin);
        return "admin/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("password") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal) {
        String username = principal.getName();
        User admin = accountService.getByUsername(username);
        if (newPassword.equals(confirmPassword)) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            accountService.saveUser(admin);
        }
        return "redirect:/admin/change-password";
    }
}