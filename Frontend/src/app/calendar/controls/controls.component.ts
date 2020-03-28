import { Component, OnInit } from '@angular/core';
import { CalendarService } from '../calendar.service';
import { Subscription } from 'rxjs/index';

@Component({
    selector: 'app-calendar-controls',
    templateUrl: './controls.component.html',
})
export class CalendarControlsComponent implements OnInit {
    display: string;
    view: number = 0;

    private subs: Subscription[];

    constructor(
        private calendar: CalendarService
    ) {
        this.subs = [];
    }

    ngOnInit() {
        const today = new Date();
        const months = [
            "January",
            "Febuary",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        ];

        console.log(`ngOnInit: Setting display to ${JSON.stringify(today)}`);
        this.display = `${months[today.getMonth()]} ${today.getFullYear()}`;
        
        this.subs.push(
            this.calendar.showDateHook.subscribe(date => {
                console.log(`showDateHook: Setting display to ${JSON.stringify(date)}`);
                this.display = `${months[date.getMonth()]} ${date.getFullYear()}`;
            })
        );
    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    changeView(view: number) {
        this.view = view;
        this.calendar.triggerChangeView(view);
    }

    next() {
        this.calendar.triggerNextPage();
    }

    prev() {
        this.calendar.triggerPrevPage();
    }

    today() {
        this.calendar.triggerSelectDate(new Date());
    }
}
