import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../user';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  private urlBase = "http://localhost:9015/bookstore";

  constructor(private http: HttpClient) { }

  //will get user joeytest
  public getAUser(): Observable<User>{
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<User>(this.urlBase+"/users/joeytest", httpHead);
  }

  public updatePassword(user:any): Observable<User>{
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.post<User>(this.urlBase+"/updatepassword",user, httpHead);
  }

}
