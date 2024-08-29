import { Customer } from "./customer.model";
import { Invoice } from "./invoice.model";
import { Product } from "./product.model";

export interface OrderItem {
  orderItemId: string
  customer: Customer
  invoice: Invoice
  product: Product
}
