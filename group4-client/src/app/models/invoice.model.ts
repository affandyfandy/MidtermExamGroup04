import { Customer } from "./customer.model";
import { OrderItem } from "./orderitem.model";

export interface Invoice {
  invoiceId: string
  totalAmount: number
  customer: Customer
  invoiceDate: string
  listOrder: OrderItem[]
}
