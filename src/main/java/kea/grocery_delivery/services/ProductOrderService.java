package kea.grocery_delivery.services;

import kea.grocery_delivery.dtos.ProductDto;
import kea.grocery_delivery.dtos.ProductOrderDto;
import kea.grocery_delivery.entities.Product;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.repositories.ProductOrderRepository;
import kea.grocery_delivery.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOrderService {
    private final ProductOrderRepository productOrderRepository;
    private final ProductRepository productRepository;

    public ProductOrderService(ProductOrderRepository productOrderRepository, ProductRepository productRepository) {
        this.productOrderRepository = productOrderRepository;
        this.productRepository = productRepository;
    }

    public List<ProductOrder> getAllProductOrders() {
        List<ProductOrder> orders = productOrderRepository.findAll();

        if (orders.isEmpty()) {
            System.out.println("No orders found");
        }
        return orders;
    }

    public Optional<ProductOrder> findOrderById(Long id) {
        return productOrderRepository.findById(id);
    }

    public ProductOrder createProductOrder(ProductOrderDto request) {
        ProductOrder newProductOrder = new ProductOrder();
        updateProductOrder(newProductOrder, request);
        productOrderRepository.save(newProductOrder);

        return newProductOrder;
    }

    private void updateProductOrder(ProductOrder original, ProductOrderDto request) {
        Optional<Product> product = productRepository.findById(request.productId());
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        original.setProduct(product.get());
        original.setQuantity(request.quantity());
    }


}
