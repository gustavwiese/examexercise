package kea.grocery_delivery.services;

import kea.grocery_delivery.entities.Delivery;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.repositories.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Optional<Delivery> findDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }

    public List<Delivery> findAllDeliveries() {
        return deliveryRepository.findAll();
    }

    public Delivery createDelivery(Delivery request) {
        Delivery newDelivery = new Delivery();
        updateDelivery(newDelivery, request);
        deliveryRepository.save(newDelivery);

        return newDelivery;
    }

    public void updateDelivery(Delivery original, Delivery request) {
        original.setDeliveryDate(request.getDeliveryDate());
        original.setFromWarehouse(request.getFromWarehouse());
        original.setDestination(request.getDestination());
    }

    public void addProductOrderToDelivery(Delivery original, ProductOrder productOrder) {
        original.getProductOrders().add(productOrder);
        deliveryRepository.save(original);
    }
}
