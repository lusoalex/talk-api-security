import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { User } from '@/_models';
import { UserService, AuthenticationService } from '@/_services';
import {ActivatedRoute, Router} from "@angular/router";

@Component({ templateUrl: 'home.component.html' })
export class HomeComponent implements OnInit {
    currentUser: User;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private userService: UserService
    ) {
        let userId = this.authenticationService.loggedUserId();
        if (userId === undefined) {
            //if not logged, then route to login page
            this.router.navigate(['/login']);
        }
        this.userService.getById(userId).subscribe(value => {
            this.currentUser = value;
        })
    }

    ngOnInit() {
    }
}