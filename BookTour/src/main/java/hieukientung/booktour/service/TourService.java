package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;
import hieukientung.booktour.model.Tour;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TourService {
    List<Tour> getAllTours();

    void saveTour(Tour tour);

    Tour getTourById(Long id);

    void deleteTour(Long id);

    Page<Tour> findPaginated(int pageNo, int pageSize);

    DestinationPoint getDestinationPointById(Long id);

    DeparturePoint getDeparturePointById(Long id);

    void saveDestinationPoint(DestinationPoint destinationPoint);

    void saveDeparturePoint(DeparturePoint departurePoint);

    List<DestinationPoint> getAllDestinationPoint();

    List<DeparturePoint> getAllDeparturePoint();
}
