import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AlertService, AuthenticationService, UserService} from "@/_services";
import {first} from "rxjs/operators";
import {User} from "@/_models";

@Component({templateUrl: 'profile.component.html'})
export class ProfileComponent implements OnInit {

    profileForm: FormGroup;
    currentUser: User;
    submitted = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private userService: UserService,
        private alertService: AlertService
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
        this.profileForm = this.formBuilder.group({
            firstName:['', Validators.required],
            lastName:['', Validators.required],
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.profileForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.profileForm.invalid) {
            return;
        }

        this.userService.update(this.currentUser)
            .pipe(first())
            .subscribe(user => {
                localStorage.setItem('currentUser', JSON.stringify(user));
                this.alertService.success("successfully updated")
            },
                err => {
                this.userService.getById(this.currentUser.id)
                    .pipe(first())
                    .subscribe(fallbackUser => {
                        this.alertService.error("failed to update, reset to initial values.",true);
                        this.currentUser = fallbackUser;
                    })
            });
    }
}