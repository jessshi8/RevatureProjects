import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../user';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {
  private urlBase = "http://localhost:9015/bookstore/register";

  constructor(private http: HttpClient) {}

  public insertUser(user:string): Observable<User[]> {
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.post<User[]>(this.urlBase, user, httpHead);
  }
}
