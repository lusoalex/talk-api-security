import {Component, OnInit} from "@angular/core";
import {MatTableDataSource} from '@angular/material';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthenticationService, UserService} from "@/_services";
import {User} from "@/_models";
import {first} from "rxjs/operators";

@Component({ templateUrl: 'admin.component.html', styleUrls: ['admin.component.css'] })
export class AdminComponent implements OnInit {

    currentUserId: string;
    displayedColumns = ['firstName', 'lastName', 'username', 'id'];
    users: User[];
    dataSource: any;

    constructor(
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private userService: UserService
    ) {
        this.currentUserId = this.authenticationService.loggedUserId()
        if (this.currentUserId === undefined) {
            //if not logged, then route to login page
            this.router.navigate(['/login']);
        }
    }

    ngOnInit() {
        this.loadAllUsers();
    }

    deleteUser(id: number) {
        this.userService.delete(id).pipe(first()).subscribe(() => {
            this.loadAllUsers()
        });
    }

    private loadAllUsers() {
        this.userService.getAll().pipe(first()).subscribe(users => {
            this.users = users;
            this.dataSource = new MatTableDataSource(users);
            this.isAdmin();
        });
    }

    /** this should never be done like this ;) **/
    private isAdmin() {
        if(this.users.filter(user => user.id==this.currentUserId && user.username=='admin').length>0) {
            this.displayedColumns = ['firstName', 'lastName', 'username', 'id', 'delete'];
        }
    }
}
