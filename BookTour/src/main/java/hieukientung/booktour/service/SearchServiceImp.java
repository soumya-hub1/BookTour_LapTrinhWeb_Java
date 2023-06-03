package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.DeparturePointRepository;
import hieukientung.booktour.repository.DestinationPointRepository;
import hieukientung.booktour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchServiceImp implements SearchService {
    @Autowired
    private DestinationPointRepository destinationPointRepository;

    @Autowired
    private DeparturePointRepository departurePointRepository;
    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<DestinationPoint> getAllDestinationPoint() {
        return destinationPointRepository.findAll();
    }

    @Override
    public List<DeparturePoint> getAllDeparturePoint() {
        return departurePointRepository.findAll();
    }

    @Override
    public List<Tour> searchTour(String departurePoint, String destinationPoint) {
        List<Tour> searchResults = tourRepository.searchTour(departurePoint, destinationPoint);
        if (searchResults.isEmpty()) {
            return null;
        }
        return searchResults;
    }

    @Override
    public List<Tour> searchTour(String departurePoint, String destinationPoint, LocalDate dateStart, LocalDate dateEnd) {
        List<Tour> searchResults = tourRepository.searchTour(departurePoint, destinationPoint, dateStart, dateEnd);
        if (searchResults.isEmpty()) {
            return null;
        }
        return searchResults;
    }
}
