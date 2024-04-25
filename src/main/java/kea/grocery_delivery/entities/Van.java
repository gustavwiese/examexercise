package kea.grocery_delivery.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Van {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private int capacityInKg;

    @OneToMany
    private List<Delivery> deliveries;

    public int getCombinedWeightInKg() {
        int totalWeight = 0;

        for (Delivery delivery : getDeliveries()) {
            totalWeight += delivery.getTotalWeightInKg();
        }
        System.out.println("Total weight in van : " + totalWeight + " kg");
        return totalWeight;
    }
}
