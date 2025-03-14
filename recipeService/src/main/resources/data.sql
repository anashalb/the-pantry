-- Truncate existing data
TRUNCATE recipes.recipe_ingredient CASCADE;
TRUNCATE recipes.recipe CASCADE;
TRUNCATE measurements.measurement_system CASCADE;
TRUNCATE measurements.measurement_unit CASCADE;
TRUNCATE measurements.measurement_type CASCADE;
TRUNCATE recipes.recipe_step CASCADE;

-- Insert measurement system and types
INSERT INTO measurements.measurement_system(name)
VALUES ('METRIC');

INSERT INTO measurements.measurement_type(name)
VALUES ('WEIGHT'),
       ('VOLUME');

-- Insert measurement units
INSERT INTO measurements.measurement_unit(name, short_name, measurement_system_id, measurement_type_id)
VALUES ('Kilograms', 'kg',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'WEIGHT')),

       ('Grams', 'g',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'WEIGHT')),

       ('Liters', 'L',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),

       ('Milliliters', 'ml',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),

       ('Teaspoons', 'tsp',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),

       ('Tablespoons', 'tbsp',
        (SELECT id FROM measurements.measurement_system LIMIT 1),
        (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME'));

-- Insert recipes
INSERT INTO recipes.recipe(recipe_id, name, description, cooking_time_minutes, preparation_time_minutes,
                           ready_in_time_minutes, servings, created_by)
VALUES ('e183275b-3653-4ba6-be4e-19e2f9afae4b', 'Kibbeh Labaniyeh',
        'A yogurt-based kibbeh dish made of bulgur, minced meat, and aromatic spices, cooked in a creamy laban (yogurt) sauce.',
        30, 60, 90, 6, 'e183275b-3653-4ba6-be4e-19e2f9afae4b'),

       ('b183275b-3653-4ba6-be4e-19e2f9afae4b', 'Dawood Basha',
        'A Middle Eastern dish featuring spiced meatballs in a rich tomato broth, often served with rice or bread.',
        40, 20, 60, 4, 'e183275b-3653-4ba6-be4e-19e2f9afae4b');

-- Insert ingredients (Replace ingredient_id values with actual IDs from your database)
INSERT INTO recipes.recipe_ingredient(ingredient_id, recipe_id, quantity, measurement_unit, preparation_method)
VALUES
    -- **Kibbeh Labaniyeh Ingredients**
    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 500,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'), 'Minced'),

    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'L'), 'Strained'),

    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 200,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'), 'Soaked and Drained'),

    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'tbsp'), 'Ground'),

    -- **Dawood Basha Ingredients**
    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 500,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'), 'Ground'),

    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 2,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'tbsp'), 'Chopped'),

    (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,
     (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'L'), 'Diced');

-- Insert cooking steps
INSERT INTO recipes.recipe_step(step_number, instructions, recipe_id)
VALUES
    -- **Kibbeh Labaniyeh Steps**
    (1, 'Prepare the kibbeh by mixing bulgur, minced meat, and spices into a smooth dough.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (2, 'Shape the kibbeh into small dumplings and set aside.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (3, 'Heat the laban (strained yogurt) with a bit of flour and egg to prevent curdling.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (4, 'Add the kibbeh dumplings to the simmering yogurt sauce and cook for 20 minutes.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),

    -- **Dawood Basha Steps**
    (1, 'Mix ground meat with chopped onions, parsley, and spices. Form into small meatballs.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (2, 'Brown the meatballs in a pan until they develop a crust.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (3, 'In another pan, heat some oil and sauté onions until soft. Add tomato paste and spices.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (4, 'Pour in water or stock and let it simmer before adding the meatballs.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),

    (5, 'Cook on low heat for 20 minutes until the sauce thickens and meatballs are fully cooked.',
     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'));