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
  showModal: boolean = false;  // Modal visibility flag
  isDetailMode: boolean = false;  // Flag to distinguish between detail and add modes
  newCustomer: Customer = { customerId: '', firstName: '', lastName: '', phone: '', active: true };  // Object to store new customer data
  selectedCustomer: Customer | null = null;  // Object to store the selected customer for detail view

  sortField: keyof Customer | 'index' = 'index';  // Field used for sorting
  sortDirection: string = 'asc'; // Sorting direction: ascending or descending
  searchTerm: string = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();  // Load customers when the component initializes
  }

  loadCustomers(): void {
    this.customerService.getCustomers(this.currentPage - 1, this.pageSize).subscribe((data) => {
      this.customers = this.sortDataArray(data.content);  // Sort customers after loading
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
    this.loadCustomers();  // Reload customers after changing sort criteria
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

  // Open the modal for adding a new customer
  openAddCustomerModal(): void {
    this.isDetailMode = false;
    this.showModal = true;
  }

  // Close the modal
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

  // Open the modal for viewing customer details or editing customer
  openCustomerDetailModal(customer: Customer, isEditMode: boolean = false): void {
    this.isDetailMode = !isEditMode;
    this.selectedCustomer = { ...customer };
    this.showModal = true;
  }

  // Save the new or edited customer
  saveCustomer(): void {
    if (this.selectedCustomer) {
        // Editing an existing customer
        this.customerService.updateCustomer(this.selectedCustomer.customerId, this.selectedCustomer).subscribe(() => {
            this.loadCustomers();
            this.closeAddCustomerModal();
        });
    } else {
        // Adding a new customer
        this.customerService.createCustomer(this.newCustomer).subscribe(() => {
            this.loadCustomers();
            this.closeAddCustomerModal();
        });
    }
  }

  // Edit customer button handler
  editCustomer(customer: Customer): void {
    this.openCustomerDetailModal(customer, true);
  }
  
}
