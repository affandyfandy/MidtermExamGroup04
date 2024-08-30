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
      .set('sortBy', `${sortBy}`)
      .set('sortOrder', `${sortOrder}`);

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


  /**
   * Searches for products based on the provided query parameters.
   * @param pageNo - The page number to retrieve.
   * @param pageSize - The number of items per page.
   * @param sortOrder - The sort order (e.g., 'asc' or 'desc').
   * @param sortBy - The field to sort by.
   * @param name - The name of the product to search for.
   * @param status - The status of the product to filter by.
   * @returns An Observable containing the search results.
   */
  searchProducts(
    pageNo: number = 0,
    pageSize: number = 10,
    sortOrder: string = 'asc',
    sortBy: string = 'name',
    name?: string,
    status?: string
  ): Observable<ProductResponse> {
    let params = new HttpParams()
      .set('pageNo', pageNo.toString())
      .set('pageSize', pageSize.toString())
      .set('sortBy', sortBy)
      .set('sortOrder', sortOrder);

    if (name) {
      params = params.set('name', name);
    }
    if (status) {
      params = params.set('status', status);
    }

    return this.http.get<ProductResponse>(`${baseUrl}/search`, { params });
  }

  /**
   * Imports products from an Excel file.
   * @param file - The Excel file to upload.
   * @returns An observable of the import response.
   */
  importProducts(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(`${baseUrl}/import`, formData, {
      observe: 'events',
      reportProgress: true,
      responseType: 'text' as 'json'
    });
  }

}
