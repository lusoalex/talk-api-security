import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import {map, mergeMap} from 'rxjs/operators';
import { JwtHelperService } from '@auth0/angular-jwt';

import { User } from '@/_models';
import {ActivatedRoute, Router} from "@angular/router";

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    constructor(private http: HttpClient,
                private route: ActivatedRoute,
                private router: Router) {

        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public get currentUserToken(): String {
        return localStorage.getItem("bearer");
    }

    login(username: string, password: string) {

        let headers = new HttpHeaders().set("Authorization","Basic " + btoa(username+":"+password));

        return this.http.post<any>(`${config.loginUrl}/authenticate`, null, {headers})
            .pipe(
                mergeMap(res => {
                    return this.logged(res.token);
                })
            )
    }

    logged(token: string) {
        localStorage.setItem('bearer', token);
        //decode bearer
        const decodedToken = new JwtHelperService().decodeToken(token);

        return this.http.get<any>(`${config.apiUrl}/users/`+decodedToken.sub).pipe(
            map(user => {
                localStorage.setItem('currentUser', JSON.stringify(user));
                return user;
            })
        );
    }

    loggedUserId() : string {
        let token = localStorage.getItem('bearer');

        if(token) {
            let decodedToken = new JwtHelperService().decodeToken(token);
            if(Date.now()/1000 > decodedToken.exp) {
                this.logout();
                return undefined;
            }
            return decodedToken.sub;
        }
        return undefined;
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        localStorage.removeItem('bearer');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }
}