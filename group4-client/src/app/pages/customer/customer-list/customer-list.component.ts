import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../../services/customer.service';
import { Customer } from '../../../models/customer.model';

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CustomerListComponent implements OnInit {
  customers: Customer[] = [];
  totalItems: number = 0;
  currentPage: number = 1;
  pageSize: number = 10;
  totalPages: number[] = [];

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getCustomers(this.currentPage - 1, this.pageSize).subscribe((data) => {
      this.customers = data.content;
      this.totalItems = data.totalElements;
      this.totalPages = Array(Math.ceil(this.totalItems / this.pageSize)).fill(0).map((x, i) => i + 1);
    });
  }

  editCustomer(customer: Customer): void {
    // Logika untuk mengedit customer
  }

  deleteCustomer(customer: Customer): void {
    if (confirm(`Are you sure you want to delete ${customer.firstName} ${customer.lastName}?`)) {
      this.customerService.deleteCustomer(customer.customerId).subscribe(() => {
        this.loadCustomers();
      });
    }
  }

  activateCustomer(customer: Customer): void {
    this.customerService.activateCustomer(customer.customerId).subscribe(() => {
      this.loadCustomers();
    });
  }

  deactivateCustomer(customer: Customer): void {
    this.customerService.deactivateCustomer(customer.customerId).subscribe(() => {
      this.loadCustomers();
    });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadCustomers();
  }
}