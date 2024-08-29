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
  showModal: boolean = false; 
  isDetailMode: boolean = false;  
  newCustomer: Customer = { customerId: '', firstName: '', lastName: '', phone: '', active: true }; 
  selectedCustomer: Customer | null = null;  

  sortField: keyof Customer | 'index' = 'index';  
  sortDirection: string = 'asc'; 
  searchTerm: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();  
  }

  loadCustomers(): void {
    this.customerService.getCustomers(this.currentPage - 1, this.pageSize).subscribe((data) => {
      this.customers = this.sortDataArray(data.content);  
      this.totalItems = data.totalElements;
      this.totalPages = Array(Math.ceil(this.totalItems / this.pageSize)).fill(0).map((x, i) => i + 1);
    });
  }

  // Sorting logic
  sortData(field: keyof Customer | 'index'): void { 
    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.loadCustomers(); 
  }

  sortDataArray(data: Customer[]): Customer[] {
    return data.sort((a: Customer, b: Customer) => {
      let comparison = 0;
      const fieldA = this.sortField === 'index' ? this.customers.indexOf(a) + 1 : a[this.sortField];
      const fieldB = this.sortField === 'index' ? this.customers.indexOf(b) + 1 : b[this.sortField];
  
      if (typeof fieldA === 'string' && typeof fieldB === 'string') {
        comparison = fieldA.toLowerCase().localeCompare(fieldB.toLowerCase());
      } else {
        comparison = fieldA > fieldB ? 1 : -1;
      }
  
      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
  }

  openAddCustomerModal(): void {
    this.isDetailMode = false;
    this.showModal = true;
  }

  closeAddCustomerModal(): void {
    this.showModal = false;
    this.newCustomer = { customerId: '', firstName: '', lastName: '', phone: '', active: true };
    this.selectedCustomer = null;
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

  toggleCustomerActivation(customer: Customer): void {
    customer.active = !customer.active;
    if (customer.active) {
      this.customerService.activateCustomer(customer.customerId).subscribe();
    } else {
      this.customerService.deactivateCustomer(customer.customerId).subscribe();
    }
  }

  openCustomerDetailModal(customer: Customer, isEditMode: boolean = false): void {
    this.isDetailMode = !isEditMode;
    this.selectedCustomer = { ...customer };
    this.showModal = true;
  }

  saveCustomer(): void {
    if (this.selectedCustomer) {
        this.customerService.updateCustomer(this.selectedCustomer.customerId, this.selectedCustomer).subscribe(() => {
            this.loadCustomers();
            this.closeAddCustomerModal();
        });
    } else {
        this.customerService.createCustomer(this.newCustomer).subscribe(() => {
            this.loadCustomers();
            this.closeAddCustomerModal();
        });
    }
  }
  
  editCustomer(customer: Customer): void {
    this.openCustomerDetailModal(customer, true);
  }
  
}
