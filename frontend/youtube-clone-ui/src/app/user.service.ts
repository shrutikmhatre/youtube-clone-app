import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {

 private userId:string='';
  constructor(private  httClient:HttpClient) {

  }

  subscribeToUser(userId:string):Observable<boolean>{
    return this.httClient.post<boolean>("http://localhost:8080/api/user/subscribe/"+userId,null);
}
  unSubscribeToUser(userId:string):Observable<boolean>{
    return this.httClient.post<boolean>("http://localhost:8080/api/user/unsubscribe/"+userId,null);
  }

  registerUser() :void   {
     this.httClient.get("http://localhost:8080/api/user/register",{responseType  : "text"}).subscribe(
         (data:string) =>{
           this.userId=data;
             console.log("logged in user user id :: -->",this.userId);
         }
     )
  }

  getUserId():string{
      console.log("logged in user user id :: -->",this.userId);
    return this.userId;
  }
}
