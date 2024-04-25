package kea.grocery_delivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    private boolean deliveryAdded = false;

    public int getTotalWeightInGrams() {
        return quantity * product.getWeightInGrams();
    }

    public Long getProductId() {
        return product.getId();
    }

}
