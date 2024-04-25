package kea.grocery_delivery.controllers;

import kea.grocery_delivery.dtos.ProductDto;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.services.ProductOrderService;
import kea.grocery_delivery.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductOrderService productOrderService;

    public ProductController(ProductService productService, ProductOrderService productOrderService) {
        this.productService = productService;
        this.productOrderService = productOrderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Optional<ProductDto> optionalProduct = productService.getProductById(id);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ProductDto> getByName(@PathVariable String name) {
        Optional<ProductDto> optionalProduct = productService.getByName(name);
        if (optionalProduct.isPresent()) {
            return ResponseEntity.ok(optionalProduct.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        if(products.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(products);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        List<ProductOrder> productOrders = productOrderService.getAllProductOrders();

        // Check if any ProductOrder has the specified product ID
        boolean hasAssociatedOrders = productOrders.stream()
                .anyMatch(order -> order.getProductId().equals(id));

        Optional<ProductDto> optionalProduct = productService.getProductById(id);

        if (optionalProduct.isPresent()) {
            if (hasAssociatedOrders) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto request) {
        if (request.id() != null || productService.getByName(request.name()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        try {
            ProductDto addedProduct = productService.addProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
        } catch (RuntimeException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto request) {
        Optional<ProductDto> productToUpdate = productService.getProductById(id);

        if (productToUpdate.isPresent() && productService.getByName(request.name()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(productService.editProduct(id, request));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }




}
