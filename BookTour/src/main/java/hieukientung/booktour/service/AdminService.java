package hieukientung.booktour.service;

import hieukientung.booktour.model.Discount;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.model.TourType;
import hieukientung.booktour.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    List<Discount> getAllDiscount();

    List<TourType> getAllTourType();

    User getByUsername(String username);

    void saveUser(User user);
}
