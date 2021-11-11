import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User } from '../user';
import { Book } from '../book';
import { BookDetailsService } from '../book-details/book-details.service';
import { BookmarksService } from './bookmarks.service';

@Component({
  selector: 'app-bookmarks',
  templateUrl: './bookmarks.component.html',
  styleUrls: ['./bookmarks.component.css']
})
export class BookmarksComponent implements OnInit {
  bookmarkList: Book[] = [];
  public sessionUser:string|null = null;
  public user:any = null;

  bookmarkGroup = new FormGroup({
    bookCover: new FormControl(''),
    bookTitle: new FormControl(''),
    bookAuthor: new FormControl(''),
    bookPublisher: new FormControl(''),
    bookPublished: new FormControl(''),
    bookGenre: new FormControl(''),
    bookPrice: new FormControl(''),
    bookSummary: new FormControl('')
  });

  constructor(private bookmarkServ:BookmarksService, private bookServ:BookDetailsService) { }

  ngOnInit(): void {
    this.sessionUser = window.sessionStorage.getItem("currentUser");
    if (this.sessionUser != null) {
      this.user = JSON.parse(this.sessionUser);
      this.bookmarkList = this.user.bookmarks;
    }
  }

  public addToCart(book:Book) {
    // remove book from bookmarks
    let user:User=this.user;
    this.bookmarkList.forEach((b, idx) => {
      if(b.isbn == book.isbn) {
        this.bookmarkList.splice(idx,1);
      }
    });
    // set user's bookmarks to updated bookmark list
    user.bookmarks=this.bookmarkList;
    // add book to cart
    user.cart.push(book);
    let stringUser = JSON.stringify(user);
    this.bookServ.addBook(stringUser).subscribe(
      response => {
        window.sessionStorage.setItem("currentUser", JSON.stringify(response));
      }
    )
  }

  public remove(book:Book) {
    let user:User=this.user;
    this.bookmarkList.forEach((b, idx) => {
      if(b.isbn == book.isbn) {
        this.bookmarkList.splice(idx,1);
      }
    });
    // set user's bookmarks to updated bookmark list
    user.bookmarks=this.bookmarkList;
    let stringUser = JSON.stringify(user);
    this.bookmarkServ.updateUser(stringUser).subscribe(
      response => {
        window.sessionStorage.setItem("currentUser", JSON.stringify(response));
      },
      error => {
        console.warn("This error occurred: " + error);
      }
    );
  }
}
