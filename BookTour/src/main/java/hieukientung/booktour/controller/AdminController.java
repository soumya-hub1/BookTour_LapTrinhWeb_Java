package hieukientung.booktour.controller;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.service.HomeService;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private TourService tourService;

    @Autowired
    private HomeService homeService;

    @GetMapping("")
    public String admin() {
        return "admin";
    }

    @PostMapping("/save")
    public String saveTour(@ModelAttribute("tour") Tour tour) {
        tourService.saveTour(tour);
        return "redirect:/admin";
    }

    @GetMapping("/update-tour/{id}")
    public String updateTour(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("listDestination", homeService.getAllDestinationPoint());
        model.addAttribute("listDeparture", homeService.getAllDeparturePoint());
        model.addAttribute("tour", tour);
        return "update-tour";
    }

    @GetMapping("/delete-tour/{id}")
    public String deleteTour(@PathVariable("id") Long id) {
        tourService.deleteTour(id);
        return "admin";
    }
}
