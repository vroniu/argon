import { Employee } from './employee.model';
export class User {
  id: number;
  username: string;
  email: string;
  employee?: Employee;
}
