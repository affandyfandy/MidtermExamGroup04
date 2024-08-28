import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../../services/product.service';

@Component({
  selector: 'app-customer-detail',
  standalone: true,
  imports: [],
  templateUrl: './customer-detail.component.html',
  styleUrl: './customer-detail.component.scss'
})
export class CustomerDetailComponent implements OnInit {
  productId: string | undefined;
  product: any;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.productId = params.get('productId')!;
      this.loadProduct();
    });
  }

  loadProduct(): void {
    if (this.productId !== undefined) {
      this.productService.getProductById(this.productId).subscribe(product => {
        this.product = product;
      })
    }
  }

}
