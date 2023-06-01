package hieukientung.booktour.service;

import hieukientung.booktour.model.DeparturePoint;
import hieukientung.booktour.model.DestinationPoint;

import java.util.List;

public interface HomeService {
    List<DestinationPoint> getAllDestinationPoint();

    List<DeparturePoint> getAllDeparturePoint();
}
