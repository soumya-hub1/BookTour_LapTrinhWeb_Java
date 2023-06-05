package hieukientung.booktour.controller;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping("/getAllTours")
    public String getAllTours(Model model) {
        model.addAttribute("listTours", tourService.getAllTours());
        return "getAllTour";
    }

    @GetMapping("/getTour/{id}")
    public String getTourById(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);

        if(tour == null) {
            return "ERROR_404";
        }
        model.addAttribute("tour", tour);
        return "getTourById";
    }
}
