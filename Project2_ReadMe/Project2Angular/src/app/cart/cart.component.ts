import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Book } from '../book';
import { BookDetailsService } from '../book-details/book-details.service';
import { User } from '../user';
import { CartService } from './cart.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartList: Book[] = [];
  public sessionUser:string|null = null;
  public user:any = null;
  public totalPrice:number = 0;
  public cartStatus:string = "";

  cartGroup = new FormGroup({
    bookCover: new FormControl(''),
    bookTitle: new FormControl(''),
    bookAuthor: new FormControl(''),
    bookPublisher: new FormControl(''),
    bookPublished: new FormControl(''),
    bookGenre: new FormControl(''),
    bookPrice: new FormControl(''),
    bookSummary: new FormControl('')
  });

  
  constructor(private cartServ:CartService, private bookServ:BookDetailsService, private router:Router) { }

  ngOnInit(): void {
    this.sessionUser = window.sessionStorage.getItem("currentUser");
    if (this.sessionUser != null) {
      this.user = JSON.parse(this.sessionUser);
      this.cartList = this.user.cart;
      this.cartList.forEach((b) => {
      this.totalPrice += b.price;
      });
    }
  }

  public addToBookmarks(book:Book) {
    // remove book from cart
    let user:User = this.user;
    this.cartList.forEach((b, idx) => {
      if(b.isbn == book.isbn) {
        this.cartList.splice(idx,1);
      }
    });
    // set user's cart to updated cart list
    user.cart = this.cartList;
    // add book to bookmarks
    user.bookmarks.push(book);
    // calculate new total price
    this.totalPrice = 0;
    this.cartList.forEach((b) => {
      this.totalPrice += b.price;
    });
    let stringUser = JSON.stringify(user);
    this.bookServ.addBook(stringUser).subscribe(
      response => {
        window.sessionStorage.setItem("currentUser", JSON.stringify(response));
      }
    )
  }

  public toCheckout() {
    if (this.cartList.length > 0) {
      this.router.navigateByUrl('/checkout');
    }
    else {
      this.cartStatus = "Cart is empty.";
    }
  }

  public remove(book:Book) {
    let user:User=this.user;
    this.cartList.forEach((b, idx) => {
      if(b.isbn == book.isbn) {
        this.cartList.splice(idx,1);
      }
    });
    // set user's cart to updated cart list
    user.cart=this.cartList;
    // calculate new total price
    this.totalPrice = 0;
    this.cartList.forEach((b) => {
      this.totalPrice += b.price;
    });
    let stringUser = JSON.stringify(user);
    this.cartServ.updateUser(stringUser).subscribe(
      response => {
        window.sessionStorage.setItem("currentUser", JSON.stringify(response));
      },
      error => {
        console.warn("This error occurred: " + error);
      }
    );
  }
}
