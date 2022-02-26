import { Employee } from 'src/app/models/employee.model';
import { Project } from "./project.model";

export class Organization {
  id?: number;
  name?: string;
  projects?: Project[];
  employees?: Employee[];
  owners?: Employee[];
  addOrganizationCard = false;
}
