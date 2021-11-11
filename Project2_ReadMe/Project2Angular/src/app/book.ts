import { Byte } from "@angular/compiler/src/util";

export class Book {
    
    constructor(public isbn: string, public book_cover: string, public title: string,
        public author: string, public publisher: string, public published: string, public genre: string, 
        public price: number, public summary: string) {
            this.isbn = isbn;
        }
        
}