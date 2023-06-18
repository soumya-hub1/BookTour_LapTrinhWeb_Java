package hieukientung.booktour.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {
    //Tour Information
    private Long id;
    private String name;
    private String image;
    private BigDecimal price;

    //Quantity of ticket in a tour
    private int quantity;

    public long getAmount() {
        return quantity * price.longValue();
    }
}