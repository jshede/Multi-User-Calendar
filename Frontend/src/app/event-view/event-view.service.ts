import { Observable } from 'rxjs';
import { Subject } from 'rxjs/index';
import { Injectable } from '@angular/core';

import { Event } from '../backend/objects';

@Injectable({
    providedIn: 'root'
})
export class EventViewService {
    private viewEventSource = new Subject<Event>();

    viewEventHook = this.viewEventSource.asObservable();

    triggerViewEvent(event: Event) {
        this.viewEventSource.next(event);
    }
}
