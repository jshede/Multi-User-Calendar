import { Injectable } from '@angular/core';
import { ServerService } from './server.service';
import { Event, EventComment} from '../objects';
import { EventData } from '../objects/event-data';
import {
    HttpClient,
    HttpRequest,
    HttpParams,
    HttpResponse,
    HttpErrorResponse,
} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class EventService {
    constructor(private server: ServerService) {}

    addEvent(event: EventData) {
        return this.server.post('addEvent', event);
    }

    getEventsByGroup(groupId: number) {
        return this.server.get<Event[]>('getEvents', { groupId });
    }

    getEventsByUser(userId: number) {
        return this.server.get<Event[]>('getEventsForUser', { userId });
    }

    updateEvent(event: Event) {
        return this.server.post('updateEvent', event);
    }

    deleteEvent(eventId: number) {
        return this.server.post('deleteEvent', { eventId });
    }

    addEventComment(eventId: number, comment: string, isPrivate: boolean) {
        return this.server.get<any>(`postEventFeedback?eventId=${eventId}&comment=${comment}&isPrivate=${isPrivate}`);
    }

    getEventComments(eventId: number) {
        return this.server.get<EventComment[]>('getEventFeedback', { eventId });
    }

    getAddress(latlng: String) {
        const key: String = 'AIzaSyAX0lFMf1ncQ0X2s3OjAUiHQ6oLz-qLrGM';
        return this.server.get2<any>(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${latlng}&key=${key}`)
    }
}
