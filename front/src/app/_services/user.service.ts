import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '@/_models';
import {Observable} from "rxjs";

@Injectable({ providedIn: 'root' })
export class UserService {
    constructor(private http: HttpClient) { }

    register(user: User) {
        return this.http.post(`${config.apiUrl}/users`, user);
    }

    getAll() {
        return this.http.get<User[]>(`${config.apiUrl}/users`);
    }

    getById(id: string) : Observable<User> {
        return this.http.get<User>(`${config.apiUrl}/users/${id}`);
    }

    update(user: User) : Observable<User> {
        return this.http.put<User>(`${config.apiUrl}/users/${user.id}`, user);
    }

    delete(id: number) {
        return this.http.delete(`${config.apiUrl}/users/${id}`);
    }
}