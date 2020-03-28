import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../app.service';
import { CalendarService } from './calendar.service';

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
})
export class CalendarComponent implements OnInit, OnDestroy {
    loading: boolean;
    loggedIn: boolean;
    selected: Date;

    // 0 for month, 1 for week, 2 for day
    view: number;

    private subs: Subscription[];

    constructor(
        private app: AppService,
        private calendar: CalendarService,
    ) {
        this.view = 0;
        this.subs = [];
    }

    ngOnInit() {
        this.loading = this.app.loading;
        this.loggedIn = this.app.loggedIn;
        this.selected = new Date();
        this.refresh();

        this.subs.push(
            this.app.refreshHook.subscribe(() => this.refresh())
        );

        this.subs.push(
            this.app.loginHook.subscribe(() => {
                this.loggedIn = true;
            })
        );

        this.subs.push(
            this.app.logoutHook.subscribe(() => {
                this.loggedIn = false;
            })
        );

        this.subs.push(
            this.calendar.changeViewHook.subscribe(view => {
                this.view = view;
            })
        );

        this.subs.push(
            this.calendar.selectDateHook.subscribe(date => {
                this.selected = date;
            })
        );
    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    refresh() {
        if (this.app.loading) return;

        this.loading = false;

        for (let group of this.app.groups) {
            if (this.calendar.groupShow[group.id] == null) {
                this.calendar.groupShow[group.id] = true;
            }
        }
    }
}
