package kea.grocery_delivery.services;

import kea.grocery_delivery.dtos.ProductDto;
import kea.grocery_delivery.entities.Product;
import kea.grocery_delivery.entities.ProductOrder;
import kea.grocery_delivery.repositories.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOrderService productOrderService;

    public ProductService(ProductRepository productRepository, ProductOrderService productOrderService) {
        this.productRepository = productRepository;
        this.productOrderService = productOrderService;
    }

    public Optional<ProductDto> getProductById(Long id) {
            return productRepository.findById(id).map(this::toDto);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new RuntimeException("No products found");
        }
        return products.stream().map(this::toDto).toList();
    }

    public Optional<ProductDto> getByName(String name) {
        return productRepository.findByName(name).map(this::toDto);
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (Exception exception) {
            throw new RuntimeException("Could not delete product", exception);
        }
    }

    public ProductDto addProduct(ProductDto request) {
        Product newProduct = new Product();
        updateProduct(newProduct, request);
        productRepository.save(newProduct);

        return toDto(newProduct);
    }

    public ProductDto editProduct(Long id, ProductDto request) {
        Product productToEdit = productRepository.findById(id).get();
        updateProduct(productToEdit, request);
        productRepository.save(productToEdit);
        return toDto(productToEdit);
    }

    public void updateProduct(Product original, ProductDto request) {
        original.setName(request.name());
        original.setPrice(request.price());
        original.setWeightInGrams(request.weightInGrams());
    }

    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getWeightInGrams());
    }
}
