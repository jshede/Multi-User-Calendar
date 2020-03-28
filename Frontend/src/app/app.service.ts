import { Observable } from 'rxjs';
import { Subject } from 'rxjs/index';
import { Injectable } from '@angular/core';

import { SignupData, User, Group, Event } from './backend/objects';

@Injectable({
    providedIn: 'root'
})
export class AppService {
    loading = false;
    loggedIn = false;
    user: User;
    groups: Group[] = [];
    events: Event[] = [];

    selectedEvent: Event;

    private loginSource = new Subject<any>();
    private logoutSource = new Subject<any>();
    private signupSource = new Subject<SignupData>();
    private refreshSource = new Subject<any>();

    loginHook = this.loginSource.asObservable();
    logoutHook = this.logoutSource.asObservable();
    signupHook = this.signupSource.asObservable();
    refreshHook = this.refreshSource.asObservable();

    triggerLogin() {
        this.loginSource.next();
    }

    triggerLogout() {
        this.logoutSource.next();
    }

    triggerSignup(signupData: SignupData) {
        this.signupSource.next(signupData);
    }

    triggerRefresh() {
        this.refreshSource.next();
    }
}
