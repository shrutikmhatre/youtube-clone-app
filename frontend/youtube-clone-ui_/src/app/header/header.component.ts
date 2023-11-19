import {Component, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent  implements  OnInit{
 isAuthenticated :boolean =false;
    constructor(private  oidcSecurityService :OidcSecurityService,private router:Router,private activatedRoute: ActivatedRoute) {

  }
  ngOnInit(): void {
  this.oidcSecurityService.isAuthenticated$.subscribe(
    ({isAuthenticated})=>{
      this.isAuthenticated = isAuthenticated;
      console.log("is user authencticated",isAuthenticated)
    }
  )
  }

  login() {
    this.oidcSecurityService.authorize();
  }
  logout() {
    this.oidcSecurityService
      .logoffAndRevokeTokens()
      .subscribe((result) => console.log(result));

    this.oidcSecurityService.logoffLocal();
  }


}
