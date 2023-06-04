package hieukientung.booktour.service;

import hieukientung.booktour.model.Tour;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    List<Tour> getAllTour();

    void saveTour(Tour tour);

    Tour getTourById(Long id);

    void deleteTour(Long id);
}
