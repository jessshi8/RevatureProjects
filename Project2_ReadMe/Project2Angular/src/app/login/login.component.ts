import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { LoginService } from './login.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loggedIn:boolean = true;
  submitted:boolean = false;
  forgot:boolean = false;
  found:boolean = true;
  success:boolean = false;

  userList: User[] = [];

  loginGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    email: new FormControl(''),
    roleid: new FormControl(''),
    orders: new FormControl([]),
    cart: new FormControl([]),
    bookmarks: new FormControl([])
  });

  emailGroup = new FormGroup({
    username: new FormControl('')
  })

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {
    this.loggedIn=true;
    this.submitted=false;
    this.forgot=false;
    this.found=true;
    this.success=false;
  }

  public login(user: FormGroup): void {
    this.submitted=true;
    let stringUser = JSON.stringify(user.value);
    this.loginService.validateUser(stringUser).subscribe(
      response => { 
        this.loggedIn=true;
        console.log(response);
        sessionStorage.setItem("currentUser", JSON.stringify(response));
        window.location.href=window.location.origin + "/index.html";
      },
      error => {
        this.loggedIn=false;
        console.warn("This error occurred: " + error);
      }
    );
  }

  forgotPassword() {
    this.forgot = true;
    this.loginService.getAllUsers().subscribe(
      response => {
        this.userList=response;
      }
    )
  }

  reset(input: FormGroup): void {
    this.found=false;
    let username = input.value.username;
    for (let user of this.userList) {
      if (user.username == username) {
        this.found = true;
        let stringUser = JSON.stringify(user);
        this.loginService.resetPassword(stringUser).subscribe(
          response => {
            this.success = true;
          },
          error => {
            console.warn("This error occurred: " + error);
          }
        )
      }
    }
  }
}
