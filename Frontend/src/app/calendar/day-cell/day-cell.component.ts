import { Component, HostListener, Input, OnInit } from '@angular/core';

import { CalendarService } from '../calendar.service';
import { Day } from '../day';

@Component({
    selector: 'app-calendar-day-cell',
    templateUrl: './day-cell.component.html',
})
export class CalendarDayCellComponent implements OnInit {
    @Input() day: Day;
    @Input() capacity: number;
    private numClicks: number;

    constructor(
        private calendar: CalendarService,
    ) {}

    ngOnInit() {
        this.numClicks = 0;
    }

    @HostListener('click')
    onClick() {
        if (!this.day.selected) {
            this.numClicks = 1;
            this.calendar.triggerSelectDate(this.day.date);
        } else {
            ++this.numClicks;
        }
    }

    @HostListener('dblclick')
    onDoubleClick() {
        if (this.numClicks >= 2) {
            this.calendar.triggerChangeView(2);
        }
    }

    immDayView() {
        this.calendar.triggerSelectDate(this.day.date);
        this.calendar.triggerChangeView(2);
    }
}
