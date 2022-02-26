import { Subproject } from "./subproject.model";

export class Project {
  id: number;
  name: string;
  organizationId: number;
  organizationName: number;
  subprojects?: Subproject[];
}
