import { Injectable } from "@angular/core";
import { AppConstants } from "../config/app.constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { BehaviorSubject, map, Observable } from "rxjs";
import { Admin } from "../models/admin.model";
import { AdminResponse } from "../models/admin-response";

const baseUrl = AppConstants.BASE_API_URL + '/login';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<Admin>;
  public currentUser: Observable<Admin>;

  constructor(private http: HttpClient) {
    console.log(localStorage.getItem('currentUser'));
    const item = localStorage.getItem('currentUser');
    this.currentUserSubject = new BehaviorSubject(item ? JSON.parse(item) : null);
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): Admin {
    return this.currentUserSubject.value;
  }

  login(username: string, password: string) {
    const httpOptions = {headers: new HttpHeaders({'Content-Type': 'application/json'})};
    const formData: FormData = new FormData();
    formData.append('username', username);
    formData.append('password', password);
    return this.http.get<AdminResponse>(baseUrl)
    .pipe(map(adminResponse => {
      localStorage.setItem('currentUser', JSON.stringify(adminResponse.data));
      this.currentUserSubject.next(adminResponse.data);
      return adminResponse.data;
    }));
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    // this.currentUserSubject.next(null);
  }


}
