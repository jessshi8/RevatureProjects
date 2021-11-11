import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { User } from '../user';
import { ProfileService } from './profile.service';
import { sha256 } from 'js-sha256';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public sessionUser:string|null = "";
  public user:any=null;
  public errorMessage:string ="";
  public successMessage:string ="";
  public passuser:User | undefined;

  passwordGroup = new FormGroup({
    currentP: new FormControl(''),
    newP: new FormControl(''),
    newP2: new FormControl('')
  });

  constructor(private profileServ:ProfileService) { }

  ngOnInit(): void {
    // Gets the current session's user
    this.sessionUser = window.sessionStorage.getItem("currentUser");
    if (this.sessionUser != null) {
      this.user = JSON.parse(this.sessionUser);
    }
  }

  public submitPassword(passwords: FormGroup){
    let user1:User =JSON.parse(this.sessionUser || '{}');
    if(passwords.value.newP.length>=5&&passwords.value.newP.length<=20){
      if(sha256(passwords.value.currentP)===user1.password){
        if(passwords.value.newP===passwords.value.newP2){
          //-------
          user1.password=passwords.value.newP;
          console.log(user1);
          let stringuser=JSON.stringify(user1);
              this.profileServ.updatePassword(stringuser).subscribe(
                response => {
                  window.sessionStorage.setItem("currentUser", JSON.stringify(response));
                },
                error =>{
                  console.warn("there was an error ", error);
                }
              )
          //-------
          this.errorMessage ="";
          this.successMessage ="Successfully updated password";
        } else {
          this.errorMessage ="New passwords do not match";
          this.successMessage ="";
        }
      } else {
        this.errorMessage ="Current password is incorrect";
        this.successMessage ="";
      }
    } else{
      this.errorMessage ="Current password incorrect number of characters";
      this.successMessage ="";
    }
  }
}
