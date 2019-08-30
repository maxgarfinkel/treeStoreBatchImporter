DROP TABLE IF EXISTS gla_trees;

CREATE TABLE gla_trees (
    gla_id VARCHAR(12) NOT NULL PRIMARY KEY,
    borough VARCHAR(50),
    species_name VARCHAR(100),
    common_name VARCHAR(50),
    display_name VARCHAR(50),
    load_date INT,
    easting INT,
    northing INT,
    longitude DOUBLE,
    latitude DOUBLE
)