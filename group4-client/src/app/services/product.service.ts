import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product } from "../models/product.model";

const baseUrl = AppConstants.BASE_API_URL + '/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  constructor(private http: HttpClient){}

  getAllProducts(): Observable<Product[]> {
    // const params = new HttpParams()
    //   .set('pageNo', pageNo.toString())
    //   .set('pageSize', pageSize.toString())
    //   .set('sortBy', sortBy)
    //   .set('sortOrder', sortOrder);
    return this.http.get<Product[]>(`${baseUrl}`);
  }

  getProductById(id: string): Observable<Product>{
    return this.http.get<Product>(`${baseUrl}/${id}`);
  }

  activateProduct(id: string): Observable<Product> {
    return this.http.post<Product>(`${baseUrl}/${id}/activate`, {});
  }

  deactivateProduct(id: string): Observable<Product> {
    return this.http.post<Product>(`${baseUrl}/${id}/deactivate`, {});
  }

  updateProduct(product: Product): Observable<Product> {
    return this.http.put<Product>(`${baseUrl}/${product.productId}`, product);
  }

  addProduct(product: Product): Observable<Product>{
    return this.http.post<Product>(`${baseUrl}`, product);
  }

}
