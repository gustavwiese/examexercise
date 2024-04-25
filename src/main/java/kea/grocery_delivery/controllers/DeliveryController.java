package kea.grocery_delivery.controllers;

import kea.grocery_delivery.dtos.DeliveryWithProductOrderDto;
import kea.grocery_delivery.entities.Delivery;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.repositories.ProductOrderRepository;
import kea.grocery_delivery.services.DeliveryService;
import kea.grocery_delivery.services.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final ProductOrderService productOrderService;
    private final ProductOrderRepository productOrderRepository;

    public DeliveryController(DeliveryService deliveryService, ProductOrderService productOrderService, ProductOrderRepository productOrderRepository) {
        this.deliveryService = deliveryService;
        this.productOrderService = productOrderService;
        this.productOrderRepository = productOrderRepository;
    }

    @PostMapping
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery request) {
        if (request.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            Delivery newDelivery = deliveryService.createDelivery(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newDelivery);
        } catch (Exception exception) {
            throw new RuntimeException("Could not create delivery", exception);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> findDelivery(@PathVariable Long id) {
        Optional<Delivery> delivery = deliveryService.findDeliveryById(id);
        if (delivery.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        System.out.println("\n"+
                            "For delivery ID: " + id + "\n" +
                            delivery.get().getTotalWeightInKg()+"\n"+
                            delivery.get().totalPrice()+
                            "\n");


        return ResponseEntity.status(HttpStatus.OK).body(delivery.get());
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> findAllDeliveries() {
        List<Delivery> deliveries = deliveryService.findAllDeliveries();
        return ResponseEntity.status(HttpStatus.OK).body(deliveries);
    }

    // Each "product order" can only be added to one delivery, which makes sense logically, since it should only be
    // delivered once.
    @PostMapping("/orders")
    public ResponseEntity<Delivery> addOrderToDelivery(@RequestBody DeliveryWithProductOrderDto request) {
        Optional<ProductOrder> order = productOrderService.findOrderById(request.productOrderId());
        Optional<Delivery> delivery = deliveryService.findDeliveryById(request.deliveryId());
        if (order.isEmpty() || delivery.isEmpty() || order.get().isDeliveryAdded()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            order.get().setDeliveryAdded(true);
            productOrderRepository.save(order.get());

            deliveryService.addProductOrderToDelivery(delivery.get() ,order.get());
            return ResponseEntity.status(HttpStatus.CREATED).body(delivery.get());
        } catch (Exception exception) {
            throw new RuntimeException("Could not add order to delivery", exception);
        }
    }


}
