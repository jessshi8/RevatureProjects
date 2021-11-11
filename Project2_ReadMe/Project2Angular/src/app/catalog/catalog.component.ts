import { Component, OnInit } from '@angular/core';
import { Book } from '../book';
import { CatalogService } from './catalog.service';

@Component({
  selector: 'app-catalog',
  templateUrl: './catalog.component.html',
  styleUrls: ['./catalog.component.css']
})
export class CatalogComponent implements OnInit {
  public keyword:string|null = null;
  public filter:any;
  public bookList: Book[] = [];

  constructor(private catalogServ:CatalogService) { }

  ngOnInit(): void {
    this.keyword = window.sessionStorage.getItem("keyword");
    var sessionFilter = window.sessionStorage.getItem("filter");
    this.filter = sessionFilter?.charAt(sessionFilter.length-2);

    if (this.keyword != null) {
      var word = JSON.parse(this.keyword);
      switch(this.filter) {
        case '1': {
          console.log("Author");
          this.catalogServ.getBooksByAuthor(word).subscribe(
            response=>{
              this.bookList=response;
            }
          );
          break;
        }
        case '2': {
          console.log("Title");
          this.catalogServ.getBooksByTitle(word).subscribe(
            response=>{
              this.bookList=response;
            }
          );
          break;
        }
        case '3': {
          console.log("ISBN");
          this.catalogServ.getBooksByISBN(word).subscribe(
            response=>{
              this.bookList = response;
            }
          );
          break;
        }
        case '4': {
          console.log("Publisher");
          this.catalogServ.getBooksByPublisher(word).subscribe(
            response=>{
              this.bookList=response;
            }
          );
          break;
        }
        case '5': {
          console.log("Genre");
          this.catalogServ.getBooksByGenre(word).subscribe(
            response=>{
              this.bookList=response;
            }
          );
          break;
        }
      }
    }
  }
}