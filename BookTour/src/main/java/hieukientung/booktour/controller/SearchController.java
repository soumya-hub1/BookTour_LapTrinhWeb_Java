package hieukientung.booktour.controller;

import hieukientung.booktour.service.SearchService;
import hieukientung.booktour.service.TourService;
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

    @Autowired
    private TourService tourService;

    @GetMapping("/search-tour")
    public String searchTour(@RequestParam("departurePoint") String departurePoint,
                             @RequestParam("destinationPoint") String destinationPoint,
                             @RequestParam("dateStart") String dateStart,
                             @RequestParam("dateEnd") String dateEnd,
                             Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (departurePoint.equals("default") || destinationPoint.equals("default")) {
            model.addAttribute("errorMessage", "Vui lòng chọn đầy đủ nơi khởi hành và nơi kết thúc");
            model.addAttribute("listDestination", tourService.getAllDestinationPoint());
            model.addAttribute("listDeparture", tourService.getAllDeparturePoint());
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
