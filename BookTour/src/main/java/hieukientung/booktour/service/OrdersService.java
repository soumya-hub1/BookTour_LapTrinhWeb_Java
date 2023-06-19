package hieukientung.booktour.service;

import hieukientung.booktour.model.Orders;
import hieukientung.booktour.model.Tour;
import hieukientung.booktour.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    public List<Orders> getAllOrders() { return ordersRepository.findAll(); }

    public void saveOrder(Orders orders) { ordersRepository.save(orders); }

    public Orders getOrdersById(Long id) {
        Optional<Orders> optional = ordersRepository.findById(id);
        Orders orders = null;
        if (optional.isPresent()) {
            orders = optional.get();
        } else {
            throw new RuntimeException("Orders not found for id :: " + id);
        }
        return orders;
    }
}
