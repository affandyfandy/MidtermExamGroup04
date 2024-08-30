import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { FormsModule } from '@angular/forms';
import { Product, ProductResponse } from '../../../models/product.model';
import { ProductFormComponent } from '../product-form/product-form.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowUp, faArrowDown, faSort } from '@fortawesome/free-solid-svg-icons';
import { HttpEvent, HttpEventType } from '@angular/common/http';
import { ToastService } from '../../../services/toast.service';
import { ToastComponent } from '../../../main/components/toast/toast.component';
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    ProductFormComponent
  ],
  templateUrl: './product-list.component.html'
})
export class ProductListComponent implements OnInit{
  products: Product[] = [];
  filteredProduct: Product[] = [];
  totalElements: number = 0;
  totalPages: number = 0;
  currentPage: number = 1;
  pageSize: number = 10;
  sortColumn: string = 'name';
  sortDirection: string = 'asc';
  isLoading: boolean = false;
  error: string | null = null;

  selectedProduct: Product | null = null;
  isModalVisible: boolean = false;
  action: string = 'add';

  searchName: string = '';
  searchPrice: string = '';
  searchStatus: string = '';

  selectedFile: File | null = null;
  message: string = '';

  // faArrowUp = faArrowUp;
  // faArrowDown = faArrowDown;
  // faSort = faSort;

  constructor(
    private productService: ProductService,
    private toastService: ToastService
  ){}

  ngOnInit(): void {
    this.loadProducts(this.currentPage);
  }

  loadProducts(page: number): void {
    this.isLoading = true;
    this.error = null;
    this.productService.getAllProducts(page-1, this.pageSize, this.sortColumn, this.sortDirection).subscribe({
      next: (response: ProductResponse) => {
        console.log("Full response:", response);
        this.products = response.content;
        this.filteredProduct = this.products;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = page;
        this.isLoading = false;
      },
      error: (err) => {
        this.error = 'Nothing to see..';
        console.error(err);
        this.isLoading = false;
        this.toastService.showToast('Failed to load products', 'error');
      }
    });
  }

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.loadProducts(this.currentPage);
    this.toastService.showToast('Retrieved products', 'success');
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.loadProducts(this.currentPage);
    // this.toastService.showToast('Changed page', 'success');
  }

  openAddProductModal(): void {
    this.selectedProduct = null;
    this.isModalVisible = true;
    this.action = "add";
  }

  openEditProductModal(product: Product): void {
    this.selectedProduct = product;
    this.isModalVisible = true;
    this.action = "edit";
  }

  detailProduct(product: Product): void {
    this.selectedProduct = product;
    this.isModalVisible = true;
    this.action = "detail";
  }


  toggleItemStatus(product: Product): void {
    if (product.active) {
      this.productService.deactivateProduct(product.productId).subscribe(
        (updatedProduct: Product) => {
          console.log('Product deactivated successfully:', updatedProduct);
          product.active = false;
          this.toastService.showToast('Product deactivated successfully!', 'success');
        },
        (error: any) => {
          console.error('Error deactivating product:', error);
          this.toastService.showToast('Error deactivating product.', 'error');
        }
      );
    } else {
      this.productService.activateProduct(product.productId).subscribe(
        (updatedProduct: Product) => {
          console.log('Product activated successfully:', updatedProduct);
          product.active = true;
          this.toastService.showToast('Product activated successfully!', 'success');
        },
        (error: any) => {
          console.error('Error activating product:', error);
          this.toastService.showToast('Error deactivating product.', 'error');
        }
      );
    }
  }

  handleCancel(): void {
    this.isModalVisible = false;
  }

  handleSave(product: Product): void {
    if (this.selectedProduct) {
      const index = this.products.findIndex(p => p.productId === this.selectedProduct!.productId);
      this.productService.updateProduct(product).subscribe((result) => {
        this.products[index] = result;
        // this.loadProducts(this.currentPage);
        this.toastService.showToast('Product updated successfully!', 'success');
      });
    } else {
      this.productService.addProduct(product).subscribe(() => {
        // this.loadProducts(this.currentPage);
        this.toastService.showToast('Product added successfully!', 'success');
      });
    }
    this.isModalVisible = false;
  }

  applyFilter(): void {
    this.productService.searchProducts(this.currentPage-1, this.pageSize, this.sortColumn, this.sortDirection, this.searchName, this.searchStatus).subscribe({
      next: (response: ProductResponse) => {
        this.products = response.content;
        this.filteredProduct = this.products;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.isLoading = false;
        this.toastService.showToast('Retrieved products', 'success');
      },
      error: (err) => {
        this.error = 'Nothing to see..';
        this.toastService.showToast('Failed to load products.', 'error');
        console.error(err);
        this.isLoading = false;
      }
    });
  }

  openImportProductModal(): void {
    this.selectedProduct = null;
    this.isModalVisible = true;
    this.action = "upload";
  }
}
