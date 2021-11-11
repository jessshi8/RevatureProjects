import { Book } from './book';

export class User {
    constructor(public username: string, public password: string, public firstname: string, 
        public lastname: string, public email: string, public roleid: string,
        public orders: Book[], public cart: Book[], public bookmarks: Book[]) { }
}