package hieukientung.booktour.service;

import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImp implements TourService{

    @Autowired
    private TourRepository tourRepository;

    @Override
    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    @Override
    public Tour getTourById(long id) {
        Optional<Tour> optional = tourRepository.findById(id);
        Tour tour = null;
        if (optional.isPresent()) {
            tour = optional.get();
        } else {
            throw new RuntimeException(" Tour not found for id :: " + id);
        }
        return tour;
    }
}
