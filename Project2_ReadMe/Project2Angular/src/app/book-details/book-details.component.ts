import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import {Book} from '../book';
import { CatalogService } from '../catalog/catalog.service';
import { User } from '../user';
import { BookDetailsService } from './book-details.service';

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: Book[] = [];
  public sessionUser:string|null = null;
  public user!: User;
  inCart!:boolean;
  inBookmarks!:boolean;
  inOrders!:boolean;
  loggedIn!:boolean;

  constructor(private route: ActivatedRoute, private catalogServ: CatalogService, 
    private bookService: BookDetailsService, private path: Router) { }

  ngOnInit(): void {
    this.inCart=false;
    this.inBookmarks=false;
    this.inOrders=false;
    this.loggedIn=false;
    //Getting the book isbn from the current route.
    const routeParams = this.route.snapshot.paramMap;
    const bookIsbnFromRoute = String(routeParams.get('bookIsbn'));

    //finding the book that correspond with the isbn provide in route.
    //this.book= Book.find(books => book.isbn === bookIsbnFromRoute);
    this.catalogServ.getBooksByISBN(bookIsbnFromRoute).subscribe(
      response=>{
        this.book=response;
        console.log(this.book[0].book_cover);
      }
    );
    this.sessionUser = window.sessionStorage.getItem("currentUser");
    if (this.sessionUser != null) {
      this.user = JSON.parse(this.sessionUser);
      console.log(this.user);
    }
  }

  public addToCart() {
    this.inBookmarks=false;
    this.inCart=false;
    this.inOrders=false;

    if (this.user != null) {
      this.loggedIn=true;
      let user:User = this.user;

      for (let b of user.bookmarks) {
        if (b.isbn == this.book[0].isbn) {
          this.inBookmarks=true;
        }
      }

      for (let b of user.cart) {
        if (b.isbn == this.book[0].isbn) {
          this.inCart=true;
        }
      }

      for (let b of user.orders) {
        if (b.isbn == this.book[0].isbn) {
          this.inOrders=true;
        }
      }

      if (!this.inCart && !this.inBookmarks && !this.inOrders) {
        user.cart.push(this.book[0]);
        console.log(user.cart);
        let stringUser = JSON.stringify(user);
        this.bookService.addBook(stringUser).subscribe(
          response => {
            window.sessionStorage.setItem("currentUser", JSON.stringify(response));
            this.path.navigate(['/cart']);
          },
          error => {
            console.warn("This error occurred: " + error);
          }
        );
      }
    } 
  }

  public addToBookmarks() {
    this.inBookmarks=false;
    this.inCart=false;
    this.inOrders
    
    if (this.user != null) {
      this.loggedIn=true;
      let user:User = this.user;

      for (let b of user.bookmarks) {
        if (b.isbn == this.book[0].isbn) {
          this.inBookmarks=true;
        }
      }

      for (let b of user.cart) {
        if (b.isbn == this.book[0].isbn) {
          this.inCart=true;
        }
      }

      for (let b of user.orders) {
        if (b.isbn == this.book[0].isbn) {
          this.inOrders=true;
        }
      }

      if (!this.inBookmarks && !this.inCart && !this.inOrders) {
        user.bookmarks.push(this.book[0]);
        let stringUser = JSON.stringify(user);
        this.bookService.addBook(stringUser).subscribe(
          response => {
            console.log(response);
            window.sessionStorage.setItem("currentUser", JSON.stringify(response));
            this.path.navigate(['/bookmarks']);
          },
          error => {
            console.warn("This error occurred: " + error);
          }
        );
      }
    } 
  }
}
