package hieukientung.booktour.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders_detail", schema = "book_tour")
public class OrdersDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_detail_id", nullable = false)
    private Long id;

    @Column(name = "price")
    private long price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders order;
}
