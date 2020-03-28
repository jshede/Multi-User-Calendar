import { Component, OnInit, OnDestroy, Input } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../../app.service';
import { CalendarService } from '../calendar.service';
import { Day } from '../day';
import { Event } from '../../backend/objects';

@Component({
    selector: 'app-month-view',
    templateUrl: './month-view.component.html',
})
export class MonthViewComponent implements OnInit, OnDestroy {
    days: Day[];

    private _selected: Date;
    private subs: Subscription[];

    constructor(
        private app: AppService,
        private calendar: CalendarService,
    ) {
        this.days = [];
        for (let i = 0; i < 6 * 7; ++i) {
            this.days[i] = {
                active: false,
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
                    this.getDate(6).getFullYear(),
                    this.getDate(6).getMonth() + 1,
                    this.getDate(6).getDate(),
                );

                this.setDate(next);
            })
        );

        this.subs.push(
            this.calendar.prevPageHook.subscribe(() => {
                const prev = new Date(
                    this.getDate(6).getFullYear(),
                    this.getDate(6).getMonth() - 1,
                    this.getDate(6).getDate(),
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
        console.log(`Refreshing MonthViewComponent`);

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
        if (this.selected != null) {
            const i = this.getIndex(this.selected);
            if (0 <= i && i < this.days.length) {
                this.days[i].selected = true;
            }
        }
    }

    setDate(date: Date) {
        let dayOfMonth = -(new Date(date.getFullYear(), date.getMonth(), 1).getDay());
        for (let day of this.days) {
            day.date = new Date(
                date.getFullYear(),
                date.getMonth(),
                ++dayOfMonth
            );
            day.selected = false;
            day.active = (day.date.getMonth() - date.getMonth()) % 12 == 0;
        }

        // Update control bar date
        this.calendar.triggerShowDate(this.getDate(6));

        // Update selected cell
        this.updateSelected();

        // Update events in cells
        this.refresh();
    }
}
