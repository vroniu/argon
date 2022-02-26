ALTER TABLE projects DROP CONSTRAINT projects_organization_fkey;
ALTER TABLE projects ADD CONSTRAINT projects_organization_fkey FOREIGN KEY (organization_id) REFERENCES public.organizations(id);