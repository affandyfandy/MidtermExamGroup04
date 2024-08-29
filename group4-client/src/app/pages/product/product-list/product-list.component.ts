import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../../services/product.service';
import { FormsModule } from '@angular/forms';
import { Product } from '../../../models/product.model';
import { ProductFormComponent } from '../product-form/product-form.component';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faPlus, faArrowUp, faArrowDown, faSort } from '@fortawesome/free-solid-svg-icons';
@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    ProductFormComponent
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit{
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faSort = faSort;

  products: Product[] = [];
  filteredProduct: Product[] = [];
  searchTerm: string = '';
  sortColumn: string = 'name';
  sortDirection: 'asc' | 'desc' = 'asc';
  isModalVisible: boolean = false;
  selectedProduct: Product | null = null;

  currentPage: number = 1;
  itemsPerPage: number = 10;

  constructor(
    private productService: ProductService,
    library: FaIconLibrary
  ){
    library.addIcons(faPlus, faArrowUp, faArrowDown, faSort);
  }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe({
      next: (data) => {
        this.products = data;
      }
    })
  }

  openAddProductModal(): void {
    this.selectedProduct = null;
    this.isModalVisible = true;
  }

  openEditProductModal(product: Product): void {
    this.selectedProduct = product;
    this.isModalVisible = true;
  }

  openDetailProductModal(product: Product): void {
    this.selectedProduct = product;
    this.isModalVisible = true;
  }

  handleCancel(): void {
    this.isModalVisible = false;
  }

  handleSave(product: Product): void {
    if (this.selectedProduct) {
      const index = this.products.findIndex(p => p.productId === this.selectedProduct!.productId);
      this.productService.updateProduct(product).subscribe((result) => {
        this.products[index] = result;
        this.filterProducts();
      });
    } else {
      this.productService.addProduct(product).subscribe(() => {
        this.loadProducts();
      });
    }
    this.isModalVisible = false;
  }

  sortBy(column: keyof Product): void {
    this.sortColumn = column;
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.filterProducts();
  }

  filterProducts(): void {
    this.filteredProduct = this.products
      .filter(item => item.name.toLowerCase().includes(this.searchTerm.toLowerCase()))
      .sort((a, b) => {
        const aValue = (a as any)[this.sortColumn];
        const bValue = (b as any)[this.sortColumn];
        const comparison = aValue < bValue ? -1 : 1;
        return this.sortDirection === 'asc' ? comparison : -comparison;
      })
      .slice((this.currentPage - 1) * this.itemsPerPage, this.currentPage * this.itemsPerPage);
  }

  toggleItemStatus(item: Product): void {
    item.isActive = !item.isActive;
    this.productService.updateProduct(item).subscribe();
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.filterProducts();
  }

  detailProduct(): void {
    this.isModalVisible = true;
  }

}
