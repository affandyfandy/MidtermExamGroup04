import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from '../models/customer.model';


@Injectable({
    providedIn: 'root',
  })
  export class CustomerService {
    private baseUrl = 'http://localhost:8080/api/v1/customer';

    constructor(private http: HttpClient) {}

    getCustomers(page: number = 0, size: number = 10): Observable<any> {
      return this.http.get<any>(`${this.baseUrl}?page=${page}&size=${size}`);
    }

    getCustomerById(id: string): Observable<Customer> {
      return this.http.get<Customer>(`${this.baseUrl}/${id}`);
    }

    createCustomer(customer: Customer): Observable<Customer> {
      return this.http.post<Customer>(this.baseUrl, customer);
    }

    updateCustomer(id: string, customer: Customer): Observable<Customer> {
      return this.http.put<Customer>(`${this.baseUrl}/${id}`, customer);
    }

    activateCustomer(id: string): Observable<Customer> {
      return this.http.post<Customer>(`${this.baseUrl}/${id}/activate`, {});
    }

    deactivateCustomer(id: string): Observable<Customer> {
      return this.http.post<Customer>(`${this.baseUrl}/${id}/deactivate`, {});
    }

    deleteCustomer(id: string): Observable<void> {
      return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }
  }
