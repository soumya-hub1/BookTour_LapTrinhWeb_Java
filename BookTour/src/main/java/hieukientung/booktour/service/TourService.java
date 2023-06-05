package hieukientung.booktour.service;

import hieukientung.booktour.model.Tour;

import java.util.List;

public interface TourService {
    List<Tour> getAllTours();

    void saveTour(Tour tour);

    Tour getTourById(Long id);

    void deleteTour(Long id);
}
