import { Component, OnInit, Input } from '@angular/core';

import { Event } from '../../../backend/objects';
import { EventViewService } from '../../../event-view/event-view.service';

@Component({
    selector: 'app-calendar-event',
    templateUrl: './event.component.html',
})
export class CalendarEventComponent implements OnInit {
    @Input() event: Event;

    bg = [
        "#c14242",
        "#fa8b23",
        "#fae423",
        "#31fa23",
        "#50f5f3",
        "#5058f5",
        "#f350f5",
        "#f5508f",
    ];

    border = [
        "#980f0f",
        "#dd7003",
        "#ddd903",
        "#27dd03",
        "#03d9dd",
        "#0336dd",
        "#ca03dd",
        "#dd036c",
    ];

    constructor(
        private service: EventViewService,
    ) {}

    ngOnInit() {}

    eventViewData() {
        console.log(`Trigger: ${JSON.stringify(this.event)}`);
        this.service.triggerViewEvent(this.event);
    }
}
