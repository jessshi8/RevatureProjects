import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../user';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) {}

  public getAllUsers(): Observable<User[]>{
    var url="http://localhost:9015/bookstore/users"
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<User[]>(url, httpHead);
  }

  public validateUser(user:string): Observable<User[]> {
    var url = "http://localhost:9015/bookstore/login";
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.post<User[]>(url, user, httpHead);
  }

  public resetPassword(user:string): Observable<User[]> {
    var url="http://localhost:9015/bookstore/resetpassword";
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.post<User[]>(url, user, httpHead);
  }
}
