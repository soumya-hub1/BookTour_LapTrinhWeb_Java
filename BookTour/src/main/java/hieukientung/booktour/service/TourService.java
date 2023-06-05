package hieukientung.booktour.service;

import hieukientung.booktour.model.Tour;

import java.util.List;

public interface TourService {
    public List<Tour> getAllTours();

    public Tour getTourById(long id);
}
