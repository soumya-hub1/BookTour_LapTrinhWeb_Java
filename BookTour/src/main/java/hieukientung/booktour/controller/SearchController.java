package hieukientung.booktour.controller;

import hieukientung.booktour.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search-tour")
    public String searchTour(@RequestParam("departurePoint") String departurePoint,
                             @RequestParam("destinationPoint") String destinationPoint,
                             @RequestParam("dateStart") String dateStart,
                             @RequestParam("dateEnd") String dateEnd,
                             Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (departurePoint.isEmpty() || destinationPoint.isEmpty()) {
            model.addAttribute("errorMessage", "Vui lòng nhập đầy đủ điểm khởi hành và điểm đến.");
            return "index";
        }

        if (!dateStart.isEmpty() && !dateEnd.isEmpty()) {
            LocalDate dateStartFormatted = LocalDate.parse(dateStart, formatter);
            LocalDate dateEndFormatted = LocalDate.parse(dateEnd, formatter);
            model.addAttribute("SearchTour", searchService.searchTour(departurePoint, destinationPoint, dateStartFormatted, dateEndFormatted));
        } else {
            model.addAttribute("SearchTour", searchService.searchTour(departurePoint, destinationPoint));
        }
        return "search-tour";
    }
}
