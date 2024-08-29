import { Admin } from "./admin.model";

export interface AdminResponse {
  message: string;
  success: boolean;
  data: Admin;
}
