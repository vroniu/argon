CREATE TABLE IF NOT EXISTS projects (
    id serial NOT NULL,
    name VARCHAR,
    CONSTRAINT projects_pkey PRIMARY KEY (id)
);