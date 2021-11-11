import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Book } from '../book';
import { BookDetailsService } from '../book-details/book-details.service';
import { User } from '../user';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  cartList: Book[] = [];
  public sessionUser:string|null = null;
  public user:any = null;
  public totalPrice:number = 0;

  constructor(private bookServ:BookDetailsService, private router:Router) { }

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

  public addToOrders() {
    let user:User = this.user;
    // move cart to orders
    this.cartList.forEach((b) => {
      user.orders.push(b);
    });
    // clear cart
    this.cartList = [];
    user.cart = this.cartList;
    // zero out price
    this.totalPrice = 0;
    let stringUser = JSON.stringify(user);
    this.bookServ.addBook(stringUser).subscribe(
      response => {
        window.sessionStorage.setItem("currentUser", JSON.stringify(response));
      }
    )
    this.router.navigateByUrl('/order-complete');
  }

  public back () {
    this.router.navigateByUrl('/cart');
  }

}
