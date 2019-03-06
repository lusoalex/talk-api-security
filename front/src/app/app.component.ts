import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AuthenticationService } from './_services';

@Component({ selector: 'app', templateUrl: 'app.component.html' })
export class AppComponent {
    currentBearer: String;

    constructor(
        private router: Router,
        private authenticationService: AuthenticationService
    ) {
        this.currentBearer = this.authenticationService.currentUserToken;
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }
}