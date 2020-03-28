import { Event } from '../backend/objects';

export interface Day {
    date: Date;
    selected: boolean;
    active: boolean;
    events: Event[];
}
