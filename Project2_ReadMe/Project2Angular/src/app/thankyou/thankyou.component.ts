import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-thankyou',
  templateUrl: './thankyou.component.html',
  styleUrls: ['./thankyou.component.css']
})
export class ThankyouComponent implements OnInit {
  public sessionUser:string|null = null;

  constructor(private router:Router) { }

  ngOnInit(): void {
    this.sessionUser = window.sessionStorage.getItem("currentUser");
  }

  public toOrders () {
    this.router.navigateByUrl('/orders');
  }

  public toSearch () {
    this.router.navigateByUrl('');
  }

}
