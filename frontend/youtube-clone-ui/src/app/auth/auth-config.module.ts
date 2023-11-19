import { NgModule } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';


@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'https://dev-iyh0eg7otf8s02jp.us.auth0.com',
            //authWellknownEndpointUrl: 'https://dev-iyh0eg7otf8s02jp.us.auth0.com',
            redirectUrl: window.location.origin,
            clientId: 'bMx3mgXbKM9yibHSyLYQkVA6R5PSp9Bw',
            scope:   'openid profile offline_access email' ,
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            //maxIdTokenIatOffsetAllowedInSeconds: 600,
            //issValidationOff: false,
           // autoUserInfo: false,
            secureRoutes : ['http://localhost:8080/'],
            customParamsAuthRequest: {
              //prompt: 'select_account', // login, consent
              audience : 'http://localhost:8080'
            },
    }
      })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
