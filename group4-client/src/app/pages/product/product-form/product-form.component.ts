import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Product } from '../../../models/product.model';
import { HttpClient, HttpEvent, HttpEventType } from '@angular/common/http';
import { ToastService } from '../../../services/toast.service';
import { ProductService } from '../../../services/product.service';
import { AppConstants } from '../../../config/app.constants';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './product-form.component.html'
})
export class ProductFormComponent implements OnInit{
  @Input() product: Product | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Product>();
  @Output() cancel = new EventEmitter<void>();

  productForm: FormGroup;
  isVisible = true;
  showFileUpload = false;
  selectedFile: File | null = null;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private toastService: ToastService,
    private productService: ProductService) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', [Validators.required, Validators.min(0)]],
      quantity: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    if (this.product) {
      console.log("quantity " + this.product.quantity);
      this.productForm.patchValue({
        name: this.product.name,
        price: this.product.price,
        quantity: this.product.quantity
      });
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = this.productForm.value;
      if (this.product) {
        productData.productId = this.product.productId;
      }

      this.save.emit(productData);
      this.onClose();
    }
  }

  onClose(): void {
    this.isVisible = false;
    this.cancel.emit();
  }

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.productService.importProducts(this.selectedFile).subscribe(
        (event: HttpEvent<any>) => {
          switch (event.type) {
            case HttpEventType.UploadProgress:
              if (event.total) {
                const percent = Math.round((100 * event.loaded) / event.total);
                this.message = `Progress: ${percent}%`;
              }
              break;
            case HttpEventType.Response:
              this.message = `Success: ${event.body}`;
              this.showFileUpload = false;
              this.toastService.showToast('Products imported successfully!', 'success');
              this.onClose();
              break;
          }
        },
        error => {
          if (error.status === 400) {
            this.message = `Error: Invalid file content - ${error.error}`;
          } else if (error.status === 500) {
            this.message = `Error: ${error.error}`;
          } else {
            this.message = `Error: ${error.message}`;
          }
          this.toastService.showToast('Failed to import products', 'error');
        }
      );
    } else {
      this.message = 'Please select a file first';
    }
  }

  toggleFileUpload(): void {
    this.showFileUpload = !this.showFileUpload;
  }
}
