import { Observable } from 'rxjs';
import { Subject } from 'rxjs/index';
import { Injectable } from '@angular/core';

import { Group, Event } from '../backend/objects';

@Injectable({
    providedIn: 'root'
})
export class CalendarService {
    groupShow: boolean[] = [];

    private nextPageSource = new Subject<any>();
    private prevPageSource = new Subject<any>();
    private selectDateSource = new Subject<Date>();
    private changeViewSource = new Subject<any>();
    private showDateSource = new Subject<any>();
    private toggleGroupSource = new Subject<Group>();

    nextPageHook = this.nextPageSource.asObservable();
    prevPageHook = this.prevPageSource.asObservable();
    selectDateHook = this.selectDateSource.asObservable();
    changeViewHook = this.changeViewSource.asObservable();
    showDateHook = this.showDateSource.asObservable();
    toggleGroupHook = this.toggleGroupSource.asObservable();

    triggerNextPage() {
        this.nextPageSource.next();
    }

    triggerPrevPage() {
        this.prevPageSource.next();
    }

    triggerSelectDate(date: Date) {
        this.selectDateSource.next(date);
    }

    triggerChangeView(view: number) {
        this.changeViewSource.next(view);
    }

    triggerShowDate(date: Date) {
        this.showDateSource.next(date);
    }

    triggerToggleGroup(group: Group) {
        this.groupShow[group.id] = !this.groupShow[group.id];
        this.toggleGroupSource.next(group);
    }

    dayDiff(a: Date, b: Date) {
        const MS_PER_DAY = 1000 * 60 * 60 * 24;

        // Discard time and timezone information
        const utc_a = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
        const utc_b = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());

        return Math.floor((utc_a - utc_b) / MS_PER_DAY);
    }
}
