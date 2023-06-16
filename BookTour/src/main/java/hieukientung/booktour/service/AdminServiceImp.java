package hieukientung.booktour.service;

import hieukientung.booktour.model.Discount;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.model.TourType;
import hieukientung.booktour.model.User;
import hieukientung.booktour.repository.DiscountRepository;
import hieukientung.booktour.repository.TourRepository;
import hieukientung.booktour.repository.TourTypeRepository;
import hieukientung.booktour.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImp implements AdminService {
    @Autowired
    private DiscountRepository discountRepository;

    @Autowired
    private TourTypeRepository tourTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Discount> getAllDiscount() {
        return discountRepository.findAll();
    }

    @Override
    public List<TourType> getAllTourType() {
        return tourTypeRepository.findAll();
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
