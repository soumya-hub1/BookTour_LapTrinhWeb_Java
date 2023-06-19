package hieukientung.booktour.repository;

import hieukientung.booktour.model.OrdersDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Long> {
}
