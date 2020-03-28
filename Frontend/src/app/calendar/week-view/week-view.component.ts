import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../../app.service';
import { CalendarService } from '../calendar.service';
import { Day } from '../day';

@Component({
    selector: 'app-week-view',
    templateUrl: './week-view.component.html',
})
export class WeekViewComponent implements OnInit, OnDestroy {
    days: Day[];
    
    private _selected: Date;
    private subs: Subscription[];

    constructor(
        private app: AppService,
        private calendar: CalendarService,
    ) {
        this.days = [];
        for (let i = 0; i < 7; ++i) {
            this.days[i] = {
                active: true,
                selected: false,
                date: null,
                events: [],
            };
        }
        
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
                    this.getDate(0).getFullYear(),
                    this.getDate(0).getMonth(),
                    this.getDate(0).getDate() + 7,
                );

                this.setDate(next);
            })
        );

        this.subs.push(
            this.calendar.prevPageHook.subscribe(() => {
                const prev = new Date(
                    this.getDate(0).getFullYear(),
                    this.getDate(0).getMonth(),
                    this.getDate(0).getDate() - 7,
                );

                this.setDate(prev);
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

    private getDate(idx: number): Date {
        return this.days[idx].date;
    }

    private getIndex(date: Date): number {
        return this.calendar.dayDiff(date, this.getDate(0));
    }

    refresh() {
        console.log(`Refreshing WeekViewComponent`);

        for (let day of this.days) {
            day.events = [];
        }

        for (let event of this.app.events) {
            if (!this.calendar.groupShow[event.groupId]) continue;
            
            const start = this.getIndex(new Date(event.start));
            const end = this.getIndex(new Date(event.end));

            for (let i = Math.max(start, 0); i <= Math.min(end, this.days.length - 1); ++i) {
                this.days[i].events.push(event);
            }
        }
    }

    get selected(): Date {
        return this._selected;
    }

    @Input('selected')
    set selected(value: Date) {
        if (this.selected != null) {
            const i = this.getIndex(this.selected);
            if (0 <= i && i < this.days.length) {
                this.days[i].selected = false;
            }
        }

        this._selected = value;

        if (value != null) {
            this.setDate(value);
        }
    }

    updateSelected() {
        if (this.selected == null) return;

        const i = this.getIndex(this.selected);
        if (0 <= i && i < this.days.length) {
            this.days[i].selected = true;
        }
    }

    setDate(date: Date) {
        let firstDay = date.getDate() - (date.getDay() % 7);
        for (let day of this.days) {
            day.date = new Date(
                date.getFullYear(),
                date.getMonth(),
                firstDay++,
            );
            day.selected = false;
        }

        // Update control bar date
        this.calendar.triggerShowDate(date);

        // Updated selected cell
        this.updateSelected();

        // Update events in cells
        this.refresh();
    }
}
