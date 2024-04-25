package kea.grocery_delivery.repositories;

import kea.grocery_delivery.entities.Van;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VanRepository extends JpaRepository<Van, Long> {
}
