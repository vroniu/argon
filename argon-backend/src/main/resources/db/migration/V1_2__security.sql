CREATE TABLE IF NOT EXISTS users (
    id serial NOT NULL,
    enabled boolean,
    username VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    employee_id int4 NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employees(id)
);

CREATE TABLE IF NOT EXISTS roles (
    id serial NOT NULL,
    authority VARCHAR NOT NULL,
    authority_description VARCHAR,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user2role (
    id serial NOT NULL,
    user_id int4 NOT NULL,
    role_id int4 NOT NULL,
    CONSTRAINT u2r_pkey PRIMARY KEY (id),
    CONSTRAINT u2r_user_fkey FOREIGN KEY (user_id) REFERENCES public.users(id),
    CONSTRAINT u2r_role_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id)
);

INSERT INTO roles (authority, authority_description) VALUES('ROLE_USER', 'App user');
INSERT INTO roles (authority, authority_description) VALUES('ROLE_ADMIN', 'App admin');