package hieukientung.booktour.controller;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.service.AdminService;
import hieukientung.booktour.service.HomeService;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@Controller
public class AdminController {
    @Autowired
    private TourService tourService;

    @Autowired
    private HomeService homeService;

    @Autowired
    private AdminService adminService;


    @GetMapping("")
    public String getAllTours(Model model) {
        return findPaginated(1, model);
    }

    @PostMapping("/save")
    public String saveTour(@ModelAttribute("tour") Tour tour) {
        tourService.saveTour(tour);
        return "redirect:/admin";
    }

    @GetMapping("/view-detail-tour/{id}")
    public String getTourById(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);

        if (tour == null) {
            return "error-404";
        }
        model.addAttribute("tour", tour);
        return "admin/view-detail-tour";
    }

    @GetMapping("/create-tour")
    public String createTour(Model model) {
        model.addAttribute("listDestination", homeService.getAllDestinationPoint());
        model.addAttribute("listDeparture", homeService.getAllDeparturePoint());
        model.addAttribute("listTourType", adminService.getAllTourType());
        model.addAttribute("listDiscount", adminService.getAllDiscount());
        Tour tour = new Tour();
        model.addAttribute("tour", tour);
        return "admin/create-tour";
    }

    @GetMapping("/update-tour/{id}")
    public String updateTour(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("listDestination", homeService.getAllDestinationPoint());
        model.addAttribute("listDeparture", homeService.getAllDeparturePoint());
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

    @GetMapping("/page/{pageNo}")
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
}