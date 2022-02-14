import { Employee } from "src/app/models/employee.model";

export class RegistrationRequest {
  username: string;
  password: string;
  email: string;
  employee: Employee;
}
