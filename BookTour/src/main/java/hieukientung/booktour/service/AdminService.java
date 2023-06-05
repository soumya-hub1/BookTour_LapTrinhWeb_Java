package hieukientung.booktour.service;

import hieukientung.booktour.model.Discount;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.model.TourType;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    List<Discount> getAllDiscount();

    List<TourType> getAllTourType();
}
