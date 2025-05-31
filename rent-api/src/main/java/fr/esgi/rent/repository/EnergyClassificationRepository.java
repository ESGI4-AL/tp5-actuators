package fr.esgi.rent.repository;

import fr.esgi.rent.domain.EnergyClassificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnergyClassificationRepository extends JpaRepository<EnergyClassificationEntity, UUID> {
    Optional<EnergyClassificationEntity> findByDesignation(String designation);
}
