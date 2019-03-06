import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

@Component({ templateUrl: 'hack.component.html' })
export class HackComponent implements OnInit {
    title = 'front-hack';
    header: string;
    payload: string;

    constructor(
        private route: ActivatedRoute
    ) {
    }

    ngOnInit(): void {
        this.route.fragment.subscribe((fragment: string) => {
            if(fragment) {
                let parts=fragment.split("&");
                let token=parts[0].replace("access_token=","");
                if(token) {
                    localStorage.setItem('bearer', token);
                    let jwt = token.split(".");
                    this.header = atob(jwt[0]);
                    this.payload = atob(jwt[1]);
                }
            }
        });
    }
}