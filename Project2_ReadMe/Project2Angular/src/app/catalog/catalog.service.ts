import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Book } from '../book';

@Injectable({
  providedIn: 'root'
})
export class CatalogService {

  constructor(private http: HttpClient) { }
  
  public getBooksByAuthor(keyword:string): Observable<Book[]>{
    var url="http://localhost:9015/bookstore/books/author/" + keyword;
    console.log(url);
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<Book[]>(url, httpHead);
  }

  public getBooksByTitle(keyword:string): Observable<Book[]>{
    var url="http://localhost:9015/bookstore/books/title/" + keyword;
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<Book[]>(url, httpHead);
  }

  public getBooksByISBN(keyword:string): Observable<Book[]>{
    var url="http://localhost:9015/bookstore/books/isbn/" + keyword;
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<Book[]>(url, httpHead);
  }

  public getBooksByPublisher(keyword:string): Observable<Book[]>{
    var url="http://localhost:9015/bookstore/books/publisher/" + keyword;
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<Book[]>(url, httpHead);
  }

  public getBooksByGenre(keyword:string): Observable<Book[]>{
    var url="http://localhost:9015/bookstore/books/genre/" + keyword;
    const httpHead = {
      headers: new HttpHeaders({
        'Content-Type':'application/json',
        'Access-Control-Allow-Origin':'*'
      })
    };
    return this.http.get<Book[]>(url, httpHead);
  }
}
