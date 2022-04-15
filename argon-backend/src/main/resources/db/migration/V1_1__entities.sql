CREATE TABLE IF NOT EXISTS employees (
    id serial NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    CONSTRAINT employees_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS organizations (
    id serial NOT NULL,
    organization_name VARCHAR NOT NULL,
    CONSTRAINT organizations_pkey PRIMARY KEY (id)
);

ALTER TABLE projects ADD COLUMN organization_id int4 NOT NULL;
ALTER TABLE projects ADD CONSTRAINT projects_organization_fkey FOREIGN KEY (organization_id) REFERENCES public.projects(id);

CREATE TABLE IF NOT EXISTS subprojects (
    id serial NOT NULL,
    subproject_name VARCHAR NOT NULL,
    project_id int4 NOT NULL,
    CONSTRAINT subprojects_pkey PRIMARY KEY (id),
    CONSTRAINT subprojects_project_fkey FOREIGN KEY (project_id) REFERENCES public.projects(id)
);

CREATE TABLE IF NOT EXISTS worktimes (
    id serial NOT NULL,
    employee_id int4 NOT NULL,
    subproject_id int4 NOT NULL,
    worktime_day TIMESTAMP NOT NULL,
    hours SMALLINT NOT NULL,
    comment VARCHAR NULL,
    CONSTRAINT worktimes_pkey PRIMARY KEY (id),
    CONSTRAINT worktimes_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employees(id),
    CONSTRAINT worktimes_subproject_fkey FOREIGN KEY (subproject_id) REFERENCES public.subprojects(id),
    CONSTRAINT worktimes_hours_unsigned CHECK (hours >= 0)
);

CREATE TABLE IF NOT EXISTS organization2employee (
    id serial NOT NULL,
    organization_id int4 NOT NULL,
    employee_id int4 NOT NULL,
    CONSTRAINT o2e_pkey PRIMARY KEY (id),
    CONSTRAINT o2e_organization_fkey FOREIGN KEY (organization_id) REFERENCES public.organizations(id),
    CONSTRAINT o2e_employee_fkey FOREIGN KEY (employee_id) REFERENCES public.employees(id)
);

CREATE TABLE IF NOT EXISTS organization_owners (
    id serial NOT NULL,
    organization_id int4 NOT NULL,
    owner_id int4 NOT NULL,
    CONSTRAINT organization_owners_pkey PRIMARY KEY (id),
    CONSTRAINT organization_owners_organization_fkey FOREIGN KEY (organization_id) REFERENCES public.organizations(id),
    CONSTRAINT organization_owners_employee_fkey FOREIGN KEY (owner_id) REFERENCES public.employees(id)
);