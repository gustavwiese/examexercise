package kea.grocery_delivery.controllers;

import kea.grocery_delivery.dtos.ProductOrderDto;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.services.ProductOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/product_orders")
public class ProductOrderController {
    private final ProductOrderService productOrderService;

    public ProductOrderController(ProductOrderService productOrderService) {
        this.productOrderService = productOrderService;
    }

    @PostMapping
    public ResponseEntity<ProductOrder> createProductOrder(@RequestBody ProductOrderDto request) {
        if (request.id() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            ProductOrder productOrder = productOrderService.createProductOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(productOrder);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
