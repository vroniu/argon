CREATE TABLE IF NOT EXISTS projects (
    id serial NOT NULL,
    project_name VARCHAR,
    CONSTRAINT projects_pkey PRIMARY KEY (id)
);