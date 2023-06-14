package hieukientung.booktour.controller;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tour")
public class TourController {

    @Autowired
    private TourService tourService;

    @GetMapping(value = {"", "/view-list-tours"})
    public String getAllTours(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/view-detail-tour/{id}")
    public String getTourById(@PathVariable("id") Long id, Model model) {
        Tour tour = tourService.getTourById(id);
        if (tour == null) {
            return "/error/404-pagenotfound";
        }
        model.addAttribute("tour", tour);
        return "view-detail-tour";
    }

    @GetMapping("/view-list-tours/page/{pageNo}")
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
        return "/view-list-tours";
    }
}
