import { Book } from './../book';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../user';

@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  public sessionUser:string|null = null;
  public user:any = null;

  constructor() { }
  
}