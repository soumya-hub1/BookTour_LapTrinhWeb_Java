package hieukientung.booktour.controller;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private TourService tourService;

    @GetMapping("/")
    public String index(Model model) {
        List<Tour> allTours = tourService.getAllTours();

        if (allTours.size() < 6 || allTours.isEmpty()) {
            return "index";
        }
        List<Tour> randomTours = randomTours(allTours, 6);
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
}

