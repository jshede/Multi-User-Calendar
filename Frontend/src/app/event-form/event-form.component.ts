import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/index';

import { AppService } from '../app.service';
import { EventService } from '../backend/services';
import { Group, Event } from '../backend/objects';
import { EventData } from '../backend/objects/event-data';



@Component({
    selector: 'app-event-form',
    templateUrl: './event-form.component.html',
})
export class EventFormComponent implements OnInit {
    groups: Group[];
    selectedGroup: Group;
    failed = false;

    form = this.fb.group({
        name:           ['', Validators.required],
        location:       ['', Validators.required],
        startDate:      ['', Validators.required],
        startTime:      ['', Validators.required],
        endDate:        ['', Validators.required],
        endTime:        ['', Validators.required],
        description:    ['', Validators.compose([Validators.required, Validators.maxLength(72)])],
        isHighPriority: ['', Validators.required],
    });

    private subs: Subscription[];

    constructor(
        private fb: FormBuilder,
        private service: EventService,
        private app: AppService,
    ) {
        this.groups = [];
        this.subs = [];
    }

    ngOnInit() {
        this.refresh();

        this.subs.push(
            this.app.refreshHook.subscribe(() => this.refresh())
        );
    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    refresh() {
        this.groups = this.app.groups;
    }

    selectGroup(group: Group) {
        this.selectedGroup = group;
    }

    createEvent() {
        // Check form validation
        if (this.form.invalid) {
            this.failed = true;
            return;
        }

        // Extract form values
        let event: EventData = this.form.value;

        event.groupId = this.selectedGroup.id;

        let s = (Object.values(this.form.get('startDate').value).join("-") +
                 " " +
                 Object.values(this.form.get('startTime').value).join(":"));

        let e = (Object.values(this.form.get('endDate').value).join("-") +
                 " " +
                 Object.values(this.form.get('endTime').value).join(":"));

        event.end = e;
        event.start = s;

        // Send event to server
        this.service.addEvent(event).subscribe(resp => { if (resp.ok) {
            console.log("Successfully created event");
        }});

        // Reset form
        this.form.reset();
        this.failed = false;
    }

    getLocation() {
        var latlong: String;
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition((position: any) => {
                latlong = position.coords.latitude + ',' + position.coords.longitude;
                this.service.getAddress(latlong).subscribe((resp:any) => {
                    if (resp.ok) {
                        this.form.get('location').setValue(resp.body.results[0].formatted_address);
                    }
                })
            });
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    }

}
