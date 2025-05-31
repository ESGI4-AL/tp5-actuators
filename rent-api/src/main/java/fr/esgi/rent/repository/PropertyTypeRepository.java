package fr.esgi.rent.repository;

import fr.esgi.rent.domain.PropertyTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyTypeRepository extends JpaRepository<PropertyTypeEntity, UUID> {
    Optional<PropertyTypeEntity> findByDesignation(String designation);
}
