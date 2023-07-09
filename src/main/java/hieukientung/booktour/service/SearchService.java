package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;
import hieukientung.booktour.model.Tour;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    List<Tour> searchTour(String departurePoint, String destinationPoint);

    List<Tour> searchTour(String departurePoint, String destinationPoint, LocalDate dateStart, LocalDate dateEnd);
}
