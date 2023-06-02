package hieukientung.booktour.controller;

import hieukientung.booktour.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search-tour")
    public String searchTour(@RequestParam("departurePoint") String departurePoint,
                             @RequestParam("destinationPoint") String destinationPoint,
                             Model model) {
        model.addAttribute("SearchTour", searchService.searchTour(departurePoint, destinationPoint));
        return "search-tour";
    }
}
