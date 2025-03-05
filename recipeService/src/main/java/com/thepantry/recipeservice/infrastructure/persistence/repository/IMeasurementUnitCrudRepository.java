package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.infrastructure.persistence.entities.MeasurementUnitEntity;
import org.springframework.data.repository.CrudRepository;

public interface IMeasurementUnitCrudRepository extends CrudRepository<MeasurementUnitEntity, Integer> {
}
