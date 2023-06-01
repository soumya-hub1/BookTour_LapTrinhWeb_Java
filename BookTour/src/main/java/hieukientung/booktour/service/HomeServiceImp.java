package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;
import hieukientung.booktour.repository.DeparturePointRepository;
import hieukientung.booktour.repository.DestinationPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImp implements HomeService {
    @Autowired
    private DestinationPointRepository destinationPointRepository;

    @Autowired
    private DeparturePointRepository departurePointRepository;

    @Override
    public List<DestinationPoint> getAllDestinationPoint() {
        return destinationPointRepository.findAll();
    }

    @Override
    public List<DeparturePoint> getAllDeparturePoint() {
        return departurePointRepository.findAll();
    }
}
