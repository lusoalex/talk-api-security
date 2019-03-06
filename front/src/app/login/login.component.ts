import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AlertService, AuthenticationService } from '@/_services';

@Component({templateUrl: 'login.component.html'})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    implicitFlow: string;
    authorizationCodeFlow: string;
    hackingRedirectUri: string;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private alertService: AlertService
    ) {
        // redirect to home if already logged in
        if (this.authenticationService.loggedUserId()) {
            this.router.navigate(['/']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });

        // get return url from route parameters or default to '/'
        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
        this.implicitFlow = "http://gateway:8092/confoo/oauth/authorize?client_id=api-security-talk&response_type=token&state=state&scope=openid&redirect_uri=http://localhost:8081/login";
        this.authorizationCodeFlow = "http://gateway:8092/confoo/oauth/authorize?client_id=api-security-talk&response_type=code&state=state&scope=openid&redirect_uri=http://localhost:8081/login";
        //this.hackingRedirectUri = "http://gateway:8092/confoo/oauth/authorize?client_id=api-security-talk&response_type=code&state=state&scope=openid&redirect_uri=http://localhost:8081/login?returnUrl=http://localhost:8082";
        this.hackingRedirectUri = "http://gateway:8092/confoo/oauth/authorize?client_id=api-security-talk&response_type=token&state=state&scope=openid&redirect_uri=http://localhost:8081/login/../hack";

        this.route.fragment.subscribe((fragment: string) => {
            if(fragment) {
                console.log("fragment: "+fragment);
                let parts=fragment.split("&");
                let token=parts[0].replace("access_token=","");
                console.log("bearer: "+token);

                this.authenticationService.logged(token)
                    .pipe(first())
                    .subscribe(
                        user => {
                            console.log("user: "+user);
                            //this.router.navigate([this.returnUrl]); //--> This is the safe way...
                            //Adding intentionally a vulnerability for the api security talk
                            location.href = this.returnUrl;
                        },
                        error => {
                            this.alertService.error(error);
                            this.loading = false;
                        });
            }
        })

        this.route.queryParams.subscribe(queryParams => {
            let code = queryParams['code'];
            let state = queryParams['state'];
            if(code) {
                this.authenticationService.code(code,state)
                    .pipe(first())
                    .subscribe(
                        data => {
                            //this.router.navigate([this.returnUrl]); //--> This is the safe way...
                            console.log(data);
                            console.log(this.returnUrl);
                            //Adding intentionally a vulnerability for the api security talk
                            location.href = this.returnUrl;
                        },
                        error => {
                            this.alertService.error(error);
                            this.loading = false;
                        });
            }
        })

        this.route.params.subscribe()
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        //https://stackoverflow.com/questions/36665234/retrieve-hash-fragment-from-url-with-angular2
        this.loading = true;
        this.authenticationService.login(this.f.username.value, this.f.password.value)
            .pipe(first())
            .subscribe(
                data => {
                    //this.router.navigate([this.returnUrl]); //--> This is the safe way...
                    console.log(data);
                    console.log(this.returnUrl);
                    //Adding intentionally a vulnerability for the api security talk
                    location.href = this.returnUrl;
                },
                error => {
                    this.alertService.error(error);
                    this.loading = false;
                });
    }
}
