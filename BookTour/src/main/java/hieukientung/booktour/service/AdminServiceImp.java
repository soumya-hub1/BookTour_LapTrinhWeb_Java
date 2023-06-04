package hieukientung.booktour.service;

import hieukientung.booktour.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImp implements AdminService {
    @Autowired
    private TourRepository tourRepository;

    @Override
    public void deleteTour(Long id) {
        tourRepository.deleteById(id);
    }
}
