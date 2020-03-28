import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../../app.service';
import { CalendarService } from '../calendar.service';
import { Day } from '../day';

@Component({
    selector: 'app-day-view',
    templateUrl: './day-view.component.html',
})
export class DayViewComponent implements OnInit, OnDestroy {
    day: Day;
    
    private _selected: Date;
    private subs: Subscription[];

    constructor(
        private app: AppService,
        private calendar: CalendarService,
    ) {
        this.day = {
            active: true,
            selected: true,
            date: null,
            events: [],
        };
        
        this.subs = [];
    }

    ngOnInit() {
        this.subs.push(
            this.app.refreshHook.subscribe(() => this.refresh())
        );

        this.subs.push(
            this.calendar.toggleGroupHook.subscribe(_ => this.refresh())
        );
        
        this.subs.push(
            this.calendar.nextPageHook.subscribe(() => {
                const next = new Date(
                    this.selected.getFullYear(),
                    this.selected.getMonth(),
                    this.selected.getDate() + 1,
                );
                
                this.calendar.triggerSelectDate(next);
            })
        );

        this.subs.push(
            this.calendar.prevPageHook.subscribe(() => {
                const prev = new Date(
                    this.selected.getFullYear(),
                    this.selected.getMonth(),
                    this.selected.getDate() - 1,
                );
                
                this.calendar.triggerSelectDate(prev);
            })
        );

        this.subs.push(
            this.calendar.selectDateHook.subscribe(date => this.setDate(date))
        );
    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    refresh() {
        console.log(`Refreshing DayViewComponent`);

        this.day.events = [];

        for (let event of this.app.events) {
            if (!this.calendar.groupShow[event.groupId]) continue;
            
            const start = new Date(event.start);
            const end = new Date(event.end);

            if (this.calendar.dayDiff(this.selected, start) >= 0
                && this.calendar.dayDiff(end, this.selected) >= 0) {
                this.day.events.push(event);
            }
        }
    }

    get selected(): Date {
        return this._selected;
    }

    @Input('selected')
    set selected(value: Date) {
        this._selected = value;

        if (value != null) {
            this.setDate(value);
        }
    }

    setDate(date: Date) {
        this.day.date = date;

        // Update control bar date
        this.calendar.triggerShowDate(date);

        // Update events in cell
        this.refresh();
    }
}
