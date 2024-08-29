import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { User } from "../models/user.model";

const API_URL = AppConstants.ADMIN_API_URL

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private http: HttpClient) {}

  /**
   * Handles Login Auth
   * @param loginRequest LoginRequest holding credentials
   * @returns Post Observable
   */
  login(loginRequest: User): Observable<any> {
    return this.http.post<any>(`${API_URL}/login`, loginRequest ).pipe(
      tap((res) => {
        localStorage.setItem(AppConstants.LOCALSTORAGE_LOGIN_ACCESS_TOKEN, res.accessToken)})
    )
  }

  /**
   * Checks if there is an user logged in
   * @returns Whether login present
   */
  isLoggedIn() {
    if (typeof window !== 'undefined') {
      return !!localStorage.getItem(AppConstants.LOCALSTORAGE_LOGIN_ACCESS_TOKEN);
    }
    return false;
  }

}
