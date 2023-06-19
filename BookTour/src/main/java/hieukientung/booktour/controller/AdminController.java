package hieukientung.booktour.controller;

import hieukientung.booktour.model.*;
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
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Destination;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

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
    public String submitSendMail(HttpServletRequest request, @RequestParam("attachment") MultipartFile multipartFile)
            throws MessagingException, UnsupportedEncodingException, InterruptedException, ExecutionException {

        // Lấy thông tin từ request
        String tourName = request.getParameter("tourName");
        String tourDescription = request.getParameter("tourDescription");

        // Lấy danh sách email
        List<Email> emails = emailService.getAllEmails();

        // Tạo một ExecutorService với số luồng tương đương số email
        ExecutorService executorService = Executors.newFixedThreadPool(emails.size());

        // Tạo danh sách các nhiệm vụ gửi email
        List<Callable<Void>> tasks = new ArrayList<>();

        // Duyệt qua danh sách email
        for (Email email : emails) {
            // Định nghĩa nhiệm vụ Callable để gửi email
            Callable<Void> task = () -> {
                // Tạo MimeMessage và MimeMessageHelper
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                // Tạo tiêu đề và nội dung email
                String subject = "Tour name: " + tourName;
                String content = "<p><b>Tour Description</b>: " + tourDescription + "<p>";

                // Cấu hình người gửi, người nhận, tiêu đề và nội dung email
                helper.setFrom("toutnest2425@gmail.com", "TourNest");
                helper.setTo(email.getEmail());
                helper.setSubject(subject);
                helper.setText(content, true);

                // Nếu có tệp đính kèm, thêm vào email
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    InputStreamSource source = new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return multipartFile.getInputStream();
                        }
                    };
                    helper.addAttachment(fileName, source);
                }

                // Gửi email
                mailSender.send(message);

                return null;
            };

            // Thêm nhiệm vụ vào danh sách tasks
            tasks.add(task);
        }

        // Chạy tất cả các nhiệm vụ và nhận danh sách Future
        List<Future<Void>> futures = executorService.invokeAll(tasks);

        // Tắt ExecutorService và đợi cho tất cả các nhiệm vụ hoàn thành
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Đảm bảo tất cả các nhiệm vụ đã hoàn thành
        for (Future<Void> future : futures) {
            future.get();
        }

        // Trả về đường dẫn tới trang hiển thị thông báo gửi email thành công
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

    @GetMapping("/view-list-destinations")
    public String getAllDestinations(Model model) {
        List<DestinationPoint> listDestinations = tourService.getAllDestinationPoint();
        model.addAttribute("listDestinations", listDestinations);
        return "admin/view-list-destinations";
    }

    @GetMapping("/view-list-destinations/update-destination/{id}")
    public String updateDestination(@PathVariable(value = "id") long id, Model model) {
        DestinationPoint destination = tourService.getDestinationPointById(id);
        model.addAttribute("destination", destination);
        return "admin/update-destination";
    }

    @PostMapping("/saveDestination")
    public String saveDestination(@ModelAttribute("destination") @Valid DestinationPoint destination) {
        tourService.saveDestinationPoint(destination);
        return "redirect:/admin/view-list-destinations";
    }

    @GetMapping("/create-destination")
    public String createDestination(Model model) {
        DestinationPoint destination = new DestinationPoint();
        model.addAttribute("destination", destination);
        return "admin/create-destination";
    }

    @GetMapping("/view-list-departures")
    public String getAllDepartures(Model model) {
        List<DeparturePoint> listDepartures = tourService.getAllDeparturePoint();
        model.addAttribute("listDepartures", listDepartures);
        return "admin/view-list-departures";
    }

    @GetMapping("/view-list-departures/update-departure/{id}")
    public String updateDeparture(@PathVariable(value = "id") long id, Model model) {
        DeparturePoint departure = tourService.getDeparturePointById(id);
        model.addAttribute("departure", departure);
        return "admin/update-departure";
    }

    @PostMapping("/saveDeparture")
    public String saveDeparture(@ModelAttribute("departure") @Valid DeparturePoint departure) {
        tourService.saveDeparturePoint(departure);
        return "redirect:/admin/view-list-departures";
    }

    @GetMapping("/create-departure")
    public String createDeparture(Model model) {
        DeparturePoint departure = new DeparturePoint();
        model.addAttribute("departure", departure);
        return "admin/create-departure";
    }
}