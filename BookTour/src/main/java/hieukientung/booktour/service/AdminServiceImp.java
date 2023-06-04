package hieukientung.booktour.service;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImp implements AdminService {
    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<Tour> getAllTour() {
        return tourRepository.findAll();
    }

    @Override
    public void saveTour(Tour tour) {
        tourRepository.save(tour);
    }

    @Override
    public Tour getTourById(Long id) {
        Optional<Tour> optional = tourRepository.findById(id);
        Tour tour = null;
        if (optional.isPresent()) {
            tour = optional.get();
        } else {
            throw new RuntimeException("Tour not found for id :: " + id);
        }
        return tour;
    }

    @Override
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }
}
