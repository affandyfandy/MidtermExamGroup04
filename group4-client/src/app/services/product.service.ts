import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product, ProductResponse } from "../models/product.model";

const baseUrl = AppConstants.BASE_API_URL + '/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private http: HttpClient){}

  /**
   * Fetches all products with pagination and sorting.
   * @param pageNo - The page number (0-based index).
   * @param pageSize - The number of items per page.
   * @param sortBy - The field to sort by.
   * @param sortOrder - The sort order ('asc' or 'desc').
   * @returns An observable of the product response.
   */
  getAllProducts(
    pageNo: number,
    pageSize: number,
    sortBy: string = 'name',
    sortOrder: string = 'asc'
  ): Observable<ProductResponse> {
    const params = new HttpParams()
      .set('pageNo', pageNo.toString())
      .set('pageSize', pageSize.toString())
      .set('sortOrder', `${sortOrder}`)
      .set('sortBy', `${sortBy}`);

    return this.http.get<ProductResponse>(baseUrl, { params });
  }

  /**
   * Fetches a product by its ID.
   * @param id - The product ID.
   * @returns An observable of the product.
   */
  getProductById(id: string): Observable<Product> {
    return this.http.get<Product>(`${baseUrl}/${id}`);
  }

  /**
   * Activates a product by its ID.
   * @param id - The product ID.
   * @returns An observable of the updated product.
   */
  activateProduct(id: string): Observable<Product> {
    return this.http.post<Product>(`${baseUrl}/${id}/activate`, {});
  }

  /**
   * Deactivates a product by its ID.
   * @param id - The product ID.
   * @returns An observable of the updated product.
   */
  deactivateProduct(id: string): Observable<Product> {
    return this.http.post<Product>(`${baseUrl}/${id}/deactivate`, {});
  }

  /**
   * Updates a product.
   * @param product - The product to update.
   * @returns An observable of the updated product.
   */
  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(`${baseUrl}/${product.productId}`, product);
  }

  /**
   * Adds a new product.
   * @param product - The product to add.
   * @returns An observable of the added product.
   */
  addProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(baseUrl, product);
  }

}
