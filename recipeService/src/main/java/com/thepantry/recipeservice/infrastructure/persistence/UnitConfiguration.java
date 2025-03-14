package com.thepantry.recipeservice.infrastructure.persistence;

import com.thepantry.recipeservice.domains.IUnitConfiguration;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class UnitConfiguration implements IUnitConfiguration {

    private static final Map<String, UnitInfo> metricUnits = new HashMap<>();
    private static final Map<String, UnitInfo> imperialUnits = new HashMap<>();

    static {
        // Volume - Metric
        metricUnits.put("ml", new UnitInfo("ml", "milliliter", UnitType.VOLUME, UnitSystem.METRIC));
        metricUnits.put("l", new UnitInfo("l", "liter", UnitType.VOLUME, UnitSystem.METRIC));
        metricUnits.put("dl", new UnitInfo("dl", "deciliter", UnitType.VOLUME, UnitSystem.METRIC));

        // Volume - Imperial
        imperialUnits.put("tsp", new UnitInfo("tsp", "teaspoon", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("tbsp", new UnitInfo("tbsp", "tablespoon", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("fl oz", new UnitInfo("fl oz", "fluid ounce", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("gill", new UnitInfo("gill", "gill", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("cup", new UnitInfo("cup", "cup", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("pt", new UnitInfo("pt", "pint", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("qt", new UnitInfo("qt", "quart", UnitType.VOLUME, UnitSystem.IMPERIAL));
        imperialUnits.put("gal", new UnitInfo("gal", "gallon", UnitType.VOLUME, UnitSystem.IMPERIAL));

        // Weight - Metric
        metricUnits.put("mg", new UnitInfo("mg", "milligram", UnitType.WEIGHT, UnitSystem.METRIC));
        metricUnits.put("g", new UnitInfo("g", "gram", UnitType.WEIGHT, UnitSystem.METRIC));
        metricUnits.put("kg", new UnitInfo("kg", "kilogram", UnitType.WEIGHT, UnitSystem.METRIC));

        // Weight - Imperial
        imperialUnits.put("lb", new UnitInfo("lb", "pound", UnitType.WEIGHT, UnitSystem.IMPERIAL));
        imperialUnits.put("oz", new UnitInfo("oz", "ounce", UnitType.WEIGHT, UnitSystem.IMPERIAL));
    }

    @Override
    public UnitInfo getUnitInfo(String unit) {
        return metricUnits.getOrDefault(unit, imperialUnits.get(unit));
    }

    @Override
    public boolean isValidUnit(String unit) {
        return metricUnits.containsKey(unit) || imperialUnits.containsKey(unit);
    }

    @Override
    public Set<String> getAllMetricUnits() {
        return metricUnits.keySet();
    }

    @Override
    public Set<String> getAllImperialUnits() {
        return imperialUnits.keySet();
    }
}