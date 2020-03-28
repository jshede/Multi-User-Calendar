import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { interval } from 'rxjs';
import { take } from 'rxjs/operators';
import { Subscription } from 'rxjs/index';

import { AppService } from './app.service';
import { AuthenticationService, GroupService, EventService } from './backend/services';
import { EventViewService } from './event-view/event-view.service';
import { SignupData, User, Group, Event } from './backend/objects';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
})
export class AppComponent implements OnInit, OnDestroy {
    title: string;
    username: string;

    private groupsDirty: number;
    private eventBuffer: Event[];
    private subs: Subscription[];

    constructor(
        private app: AppService,
        private authServer: AuthenticationService,
        private groupServer: GroupService,
        private eventServer: EventService,
        private eventView: EventViewService,
        private router: Router,
    ) {
        this.title = 'Koalendar';
        this.username = 'not logged in';

        this.groupsDirty = 0;
        this.eventBuffer = [];
        this.subs = [];
    }

    ngOnInit() {
        this.subs.push(
            this.app.signupHook.subscribe(signupData => this.loginFromSignup(signupData))
        );

        this.subs.push(
            this.app.loginHook.subscribe(() => {
                this.app.loading = true;
                this.app.loggedIn = true;
                this.router.navigate(['/calendar']);
                this.requestUser();
                this.requestRefresh();
            })
        );

        this.subs.push(
            this.app.logoutHook.subscribe(() => {
                this.app.loading = false;
                this.app.loggedIn = false;
                this.username = 'not logged in';
            })
        );

        this.subs.push(
            interval(20000).subscribe(() => this.requestRefresh())
        );

        this.subs.push(
            this.eventView.viewEventHook.subscribe(event => {
                this.app.selectedEvent = event;
                this.router.navigate(['/event-view']);
            })
        );
    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    requestUser() {
        this.authServer.getCurrentUser()
            .subscribe(resp => { if (resp.ok) {
                this.app.user = resp.body;
                this.username = resp.body.username;
            }});
    }

    loginFromSignup(signupData: SignupData) {
        const { username, passwordHash } = signupData;
        this.authServer.login({ username, password: passwordHash })
            .subscribe(resp => { if (resp.ok) {
                this.app.triggerLogin();
            }});
    }

    private requestRefreshGroupEvents(group: Group) {
        this.eventServer.getEventsByGroup(group.id)
            .pipe(take(1))
            .subscribe(resp => { if (resp.ok) {
                this.eventBuffer = this.eventBuffer.concat(resp.body);


                if (--this.groupsDirty <= 0) {
                    this.app.events = this.eventBuffer;
                    this.eventBuffer = [];

                    this.app.loading = false;
                    this.app.triggerRefresh();
                }
            }});
    }

    requestRefresh() {
        // Return if there is already a pending refresh request
        if (this.groupsDirty >= 1) return;

        // Return if not logged in
        if (!this.app.loggedIn) return;

        console.log('Polling server...');

        this.groupServer.getGroups()
            .pipe(take(1))
            .subscribe(
                resp => { if (resp.ok) {
                    this.app.groups = resp.body;
                    this.groupsDirty = resp.body.length;


                    for (let group of resp.body) {
                        this.requestRefreshGroupEvents(group);
                    }

                    if (this.groupsDirty <= 0) {
                        this.app.events = [];
                        this.app.loading = false;
                        this.app.triggerRefresh();
                    }
                }});
    }
}
