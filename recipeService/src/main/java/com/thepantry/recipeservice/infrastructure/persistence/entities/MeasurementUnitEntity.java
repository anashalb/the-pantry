package com.thepantry.recipeservice.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "measurement_unit", schema = "measurements")
public class MeasurementUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "short_name", nullable = false, length = 10)
    private String shortName;

    @ColumnDefault("false")
    @Column(name = "is_base_unit")
    private Boolean isBaseUnit;

    @ColumnDefault("1.0")
    @Column(name = "conversion_factor", nullable = false)
    private Double conversionFactor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "measurement_type_id", nullable = false)
    private MeasurementTypeEntity measurementType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "measurement_system_id", nullable = false)
    private MeasurementSystemEntity measurementSystem;

    @OneToMany(mappedBy = "measurementUnit")
    private Set<IngredientEntity> ingredients = new LinkedHashSet<>();

    @OneToMany(mappedBy = "measurementUnit")
    private Set<RecipeIngredientEntity> recipeIngredients = new LinkedHashSet<>();

}