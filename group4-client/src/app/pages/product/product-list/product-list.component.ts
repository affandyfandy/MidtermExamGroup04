import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { FormsModule } from '@angular/forms';
import { Product, ProductResponse } from '../../../models/product.model';
import { ProductFormComponent } from '../product-form/product-form.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faArrowUp, faArrowDown, faSort } from '@fortawesome/free-solid-svg-icons';
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

  // faArrowUp = faArrowUp;
  // faArrowDown = faArrowDown;
  // faSort = faSort;

  constructor(
    private productService: ProductService
  ){}

  ngOnInit(): void {
    this.loadProducts(this.currentPage);
  }

  loadProducts(page: number): void {
    this.isLoading = true;
    this.error = null;
    this.productService.getAllProducts(page, this.pageSize, this.sortColumn, this.sortDirection).subscribe({
      next: (response: ProductResponse) => {
        this.products = response.content;
        this.filteredProduct = this.products;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.currentPage = page;
        this.isLoading = false;
        console.log("total pages " + this.totalPages);
      },
      error: (err) => {
        this.error = 'Failed to load products. Please try again later.';
        console.error(err);
        this.isLoading = false;
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
  }

  changePage(page: number): void {
    this.currentPage = page;
    console.log("current page " + this.currentPage);
    this.loadProducts(this.currentPage);
  }

  openAddProductModal(): void {
    this.selectedProduct = null;
    this.isModalVisible = true;
    console.log("add product modal");
  }

  openEditProductModal(product: Product): void {
    // Implement your modal logic here
  }

  detailProduct(product: Product): void {
    // Implement your detail view logic here
  }

  toggleItemStatus(product: Product): void {
    product.active = !product.active;
    this.productService.updateProduct(product).subscribe({
      next: () => this.loadProducts(this.currentPage),
      error: err => {
        console.error('Failed to update product status', err);
      }
    });
  }

  handleCancel(): void {
    this.isModalVisible = false;
  }

  handleSave(product: Product): void {
    if (this.selectedProduct) {
      const index = this.products.findIndex(p => p.productId === this.selectedProduct!.productId);
      this.productService.updateProduct(product).subscribe((result) => {
        this.products[index] = result;
        this.loadProducts(this.currentPage);
      });
    } else {
      this.productService.addProduct(product).subscribe(() => {
        this.loadProducts(this.currentPage);
      });
    }
    this.isModalVisible = false;
  }
}
