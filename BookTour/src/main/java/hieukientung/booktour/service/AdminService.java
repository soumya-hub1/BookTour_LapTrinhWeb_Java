package hieukientung.booktour.service;

import hieukientung.booktour.model.Discount;
import hieukientung.booktour.model.TourType;

import java.util.List;

public interface AdminService {
    List<Discount> getAllDiscount();

    List<TourType> getAllTourType();
}