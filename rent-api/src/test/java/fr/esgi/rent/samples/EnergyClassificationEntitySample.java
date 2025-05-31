package fr.esgi.rent.samples;

import fr.esgi.rent.domain.EnergyClassificationEntity;

import java.util.UUID;

public class EnergyClassificationEntitySample {

    public static EnergyClassificationEntity classificationA() {
        EnergyClassificationEntity classification = new EnergyClassificationEntity();
        classification.setId(UUID.fromString("b50e8400-e29b-41d4-a716-446655440001"));
        classification.setDesignation("A");
        return classification;
    }

    public static EnergyClassificationEntity classificationB() {
        EnergyClassificationEntity classification = new EnergyClassificationEntity();
        classification.setId(UUID.fromString("b50e8400-e29b-41d4-a716-446655440002"));
        classification.setDesignation("B");
        return classification;
    }

    public static EnergyClassificationEntity classificationC() {
        EnergyClassificationEntity classification = new EnergyClassificationEntity();
        classification.setId(UUID.fromString("b50e8400-e29b-41d4-a716-446655440003"));
        classification.setDesignation("C");
        return classification;
    }

    public static EnergyClassificationEntity classificationD() {
        EnergyClassificationEntity classification = new EnergyClassificationEntity();
        classification.setId(UUID.fromString("b50e8400-e29b-41d4-a716-446655440004"));
        classification.setDesignation("D");
        return classification;
    }
}
