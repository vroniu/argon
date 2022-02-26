import { Subproject } from './subproject.model';
export class Worktime {
  id?: number;
  employeeId?: number;
  employeeName?: string;
  subproject?: Subproject
  comment?: string
  day: Date;
  hours: number;
}
