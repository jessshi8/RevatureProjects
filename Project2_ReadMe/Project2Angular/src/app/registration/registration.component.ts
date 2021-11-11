import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { RegistrationService } from './registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  public valid:boolean = true;

  registerGroup = new FormGroup({
    username: new FormControl(''),
    password: new FormControl(''),
    firstname: new FormControl(''),
    lastname: new FormControl(''),
    email: new FormControl(''),
    roleid: new FormControl('Customer'),
    orders: new FormControl([]),
    cart: new FormControl([]),
    bookmarks: new FormControl([])
  });

  constructor(private registrationService: RegistrationService) { }

  ngOnInit(): void {
    this.valid=true;
  }

  public submitUser(user: FormGroup) {
    if (this.validateEmail(user.value.email)) {
      this.valid=true;
      let stringUser = JSON.stringify(user.value);
      this.registrationService.insertUser(stringUser).subscribe(
        response => {
          window.location.href=window.location.origin + "/index.html";
        },
        error => {
          console.warn("This error occurred: " + error);
        }
      )
    } else {
      this.valid=false;
    }
  }

  public validateEmail(email:string) {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
  }

}
