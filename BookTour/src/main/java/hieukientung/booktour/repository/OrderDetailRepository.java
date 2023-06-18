package hieukientung.booktour.repository;

import hieukientung.booktour.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrdersDetail, Long> {
}
