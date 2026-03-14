-- =====================
-- Schemas
-- =====================
CREATE SCHEMA IF NOT EXISTS measurements;
CREATE SCHEMA IF NOT EXISTS recipes;
CREATE SCHEMA IF NOT EXISTS categories;

-- =====================
-- measurements schema
-- =====================
CREATE TABLE measurements.measurement_system (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE measurements.measurement_type (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE measurements.measurement_unit (
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(50)       NOT NULL,
    short_name           VARCHAR(10)       NOT NULL,
    is_base_unit         BOOLEAN           DEFAULT FALSE,
    conversion_factor    DOUBLE PRECISION  DEFAULT 1.0 NOT NULL,
    measurement_type_id  INTEGER           NOT NULL REFERENCES measurements.measurement_type (id) ON DELETE CASCADE,
    measurement_system_id INTEGER          NOT NULL REFERENCES measurements.measurement_system (id) ON DELETE CASCADE
);

-- =====================
-- categories schema
-- =====================
CREATE TABLE categories.category (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL,
    description TEXT
);

-- =====================
-- recipes schema
-- =====================
CREATE TABLE recipes.recipe (
    id                       BIGSERIAL PRIMARY KEY,
    recipe_id                UUID         NOT NULL UNIQUE,
    created_by               UUID,
    name                     VARCHAR(100) NOT NULL,
    description              VARCHAR(500),
    servings                 SMALLINT,
    cooking_time_minutes     BIGINT,
    ready_in_time_minutes    BIGINT,
    preparation_time_minutes BIGINT,
    created_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recipes.ingredient (
    id                  SERIAL PRIMARY KEY,
    ingredient_id       INTEGER      NOT NULL,
    name                VARCHAR(100) NOT NULL,
    measurement_unit_id INTEGER      NOT NULL REFERENCES measurements.measurement_unit (id) ON DELETE SET NULL
);

CREATE TABLE recipes.recipe_ingredient (
    id                 BIGSERIAL PRIMARY KEY,
    recipe_id          BIGINT           NOT NULL REFERENCES recipes.recipe (id) ON DELETE CASCADE,
    ingredient_id      UUID             NOT NULL,
    quantity           DOUBLE PRECISION NOT NULL,
    preparation_method VARCHAR(256),
    measurement_unit   VARCHAR          NOT NULL
);

CREATE TABLE recipes.recipe_step (
    id           BIGSERIAL PRIMARY KEY,
    recipe_id    BIGINT  NOT NULL REFERENCES recipes.recipe (id) ON DELETE CASCADE,
    step_number  INTEGER NOT NULL,
    instructions TEXT    NOT NULL
);

-- =====================
-- Seed data
-- =====================
INSERT INTO measurements.measurement_system (name) VALUES ('METRIC');

INSERT INTO measurements.measurement_type (name) VALUES ('WEIGHT'), ('VOLUME');

INSERT INTO measurements.measurement_unit (name, short_name, measurement_system_id, measurement_type_id)
VALUES ('Kilograms',   'kg',   (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'WEIGHT')),
       ('Grams',       'g',    (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'WEIGHT')),
       ('Liters',      'L',    (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),
       ('Milliliters', 'ml',   (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),
       ('Teaspoons',   'tsp',  (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME')),
       ('Tablespoons', 'tbsp', (SELECT id FROM measurements.measurement_system LIMIT 1), (SELECT id FROM measurements.measurement_type WHERE name = 'VOLUME'));

INSERT INTO recipes.recipe (recipe_id, name, description, cooking_time_minutes, preparation_time_minutes, ready_in_time_minutes, servings, created_by)
VALUES ('e183275b-3653-4ba6-be4e-19e2f9afae4b', 'Kibbeh Labaniyeh',
        'A yogurt-based kibbeh dish made of bulgur, minced meat, and aromatic spices, cooked in a creamy laban (yogurt) sauce.',
        30, 60, 90, 6, 'e183275b-3653-4ba6-be4e-19e2f9afae4b'),
       ('b183275b-3653-4ba6-be4e-19e2f9afae4b', 'Dawood Basha',
        'A Middle Eastern dish featuring spiced meatballs in a rich tomato broth, often served with rice or bread.',
        40, 20, 60, 4, 'e183275b-3653-4ba6-be4e-19e2f9afae4b');

INSERT INTO recipes.recipe_ingredient (ingredient_id, recipe_id, quantity, measurement_unit, preparation_method)
VALUES (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 500,  (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'),    'Minced'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,    (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'L'),    'Strained'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 200,  (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'),    'Soaked and Drained'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,    (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'tbsp'), 'Ground'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 500,  (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'g'),    'Ground'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 2,    (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'tbsp'), 'Chopped'),
       (gen_random_uuid(), (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'), 1,    (SELECT short_name FROM measurements.measurement_unit WHERE short_name = 'L'),    'Diced');

INSERT INTO recipes.recipe_step (step_number, instructions, recipe_id)
VALUES (1, 'Prepare the kibbeh by mixing bulgur, minced meat, and spices into a smooth dough.',        (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (2, 'Shape the kibbeh into small dumplings and set aside.',                                     (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (3, 'Heat the laban (strained yogurt) with a bit of flour and egg to prevent curdling.',        (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (4, 'Add the kibbeh dumplings to the simmering yogurt sauce and cook for 20 minutes.',          (SELECT id FROM recipes.recipe WHERE recipe_id = 'e183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (1, 'Mix ground meat with chopped onions, parsley, and spices. Form into small meatballs.',     (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (2, 'Brown the meatballs in a pan until they develop a crust.',                                 (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (3, 'In another pan, heat some oil and sauté onions until soft. Add tomato paste and spices.',  (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (4, 'Pour in water or stock and let it simmer before adding the meatballs.',                    (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b')),
       (5, 'Cook on low heat for 20 minutes until the sauce thickens and meatballs are fully cooked.', (SELECT id FROM recipes.recipe WHERE recipe_id = 'b183275b-3653-4ba6-be4e-19e2f9afae4b'));
