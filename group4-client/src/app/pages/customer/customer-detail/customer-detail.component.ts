import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Customer } from '../../../models/customer.model';

@Component({
  selector: 'app-customer-detail',
  templateUrl: './customer-detail.component.html',
  styleUrls: ['./customer-detail.component.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class CustomerDetailComponent {
  @Input() customer: Customer | null = null;
  @Input() isDetailMode: boolean = false; // Determine if the modal is for viewing details or adding a new customer
  @Output() customerAdded = new EventEmitter<Customer>();
  @Output() close = new EventEmitter<void>();

  newCustomer: Customer = {
    customerId: '',
    firstName: '',
    lastName: '',
    phone: '',
    active: false,
  };

  get isAdding(): boolean {
    return !this.isDetailMode;
  }

  openModal(customer?: Customer) {
    this.isDetailMode = !!customer;
    this.customer = customer ? { ...customer } : null;
    this.newCustomer = this.customer ? this.customer : {
      customerId: '',
      firstName: '',
      lastName: '',
      phone: '',
      active: false,
    };
  }

  closeModal() {
    this.isDetailMode = false;
    this.customer = null;
    this.close.emit();
  }

  saveCustomer() {
    if (this.isAdding) {
      this.customerAdded.emit(this.newCustomer);
    }
    this.closeModal();
  }
}
