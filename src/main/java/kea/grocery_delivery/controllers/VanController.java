package kea.grocery_delivery.controllers;

import kea.grocery_delivery.dtos.VanWithDeliveryDto;
import kea.grocery_delivery.entities.Delivery;
import kea.grocery_delivery.entities.Van;
import kea.grocery_delivery.services.DeliveryService;
import kea.grocery_delivery.services.VanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/vans")
public class VanController {
    private final VanService vanService;
    private final DeliveryService deliveryService;

    public VanController(VanService vanService, DeliveryService deliveryService) {
        this.vanService = vanService;
        this.deliveryService = deliveryService;
    }

    @PostMapping("/deliveries")
    public ResponseEntity<Van> addDeliveryToVan(@RequestBody VanWithDeliveryDto request) {
        Optional<Van> van =  vanService.findById(request.vanId());
        Optional<Delivery> delivery = deliveryService.findDeliveryById(request.deliveryId());

        if (van.isEmpty() || delivery.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

        vanService.addDeliveryToVan(van.get(), delivery.get());
        return ResponseEntity.status(HttpStatus.OK).body(van.get());
    }
}
