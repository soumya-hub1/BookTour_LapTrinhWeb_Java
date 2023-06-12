package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.DeparturePointRepository;
import hieukientung.booktour.repository.DestinationPointRepository;
import hieukientung.booktour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TourServiceImp implements TourService {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private DestinationPointRepository destinationPointRepository;

    @Autowired
    private DeparturePointRepository departurePointRepository;

    @Override
    public List<Tour> getAllTours() {
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

    @Override
    public Page<Tour> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.tourRepository.findAll(pageable);
    }

    @Override
    public List<DestinationPoint> getAllDestinationPoint() {
        return destinationPointRepository.findAll();
    }

    @Override
    public List<DeparturePoint> getAllDeparturePoint() {
        return departurePointRepository.findAll();
    }
}
