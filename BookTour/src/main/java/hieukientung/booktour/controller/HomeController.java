package hieukientung.booktour.controller;

import hieukientung.booktour.model.Email;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.EmailRepository;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private TourService tourService;

    @Autowired
    private EmailRepository emailRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Tour> allTours = tourService.getAllTours();

        if (allTours.size() < 3 || allTours.isEmpty()) {
            return "index";
        }
        List<Tour> randomTours = randomTours(allTours, 3);
        model.addAttribute("listDestination", tourService.getAllDestinationPoint());
        model.addAttribute("listDeparture", tourService.getAllDeparturePoint());
        model.addAttribute("listTours", randomTours);
        return "index";
    }

    private List<Tour> randomTours(List<Tour> allTours, int count) {
        List<Tour> tourCopy = new ArrayList<>(allTours);
        Collections.shuffle(tourCopy);
        return tourCopy.subList(0, count);
    }

    @PostMapping("/")
    public String saveEmail(@RequestParam("email") String email, Model model) {
        Email existingEmail = emailRepository.findByEmail(email);
        if (existingEmail != null) {
            model.addAttribute("errorMessage", "Email đã tồn tại!");
        } else {
            Email newEmail = new Email();
            newEmail.setEmail(email);
            emailRepository.save(newEmail);
            model.addAttribute("successMessage", "Cảm ơn bạn đã đăng ký!");
        }
        return "index";
    }
}