import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../app.service';
import { EventViewService } from './event-view.service';
import { AuthenticationService, EventService } from '../backend/services';
import { Event } from '../backend/objects';

@Component({
    selector: 'app-event-view',
    templateUrl: './event-view.component.html'
})
export class EventViewComponent implements OnInit, OnDestroy {
    event: Event;
    ws: WebSocket;
    userId: number;
    eventId: number;
    messageToSend: string;
    private subs: Subscription[];

    constructor(
        private authServer: AuthenticationService,
        private eventServer: EventService,
        private app: AppService,
        private service: EventViewService,
    ) {
        this.subs = [];
    }

    ngOnInit() {

        this.event = this.app.selectedEvent;
        this.eventId = this.app.selectedEvent.id;
        this.subs.push(
            this.service.viewEventHook.subscribe(event => {
                this.event = event;
            })
        );

        this.authServer.getUserId().subscribe(resp => {
            if(resp.ok){
                this.connect(resp.body+"");
            }
        });

    }

    ngOnDestroy() {
        for (let sub of this.subs) {
            sub.unsubscribe();
        }
    }

    postMessage(message: string) {
        if(message === ""){
            alert("Please enter a valid message");
            return;
        }
        this.addMessageToChat(message, true);
        this.eventServer.addEventComment(this.eventId, encodeURI(message), false).subscribe(resp => {
            console.log(resp);
            if(resp.ok){
                console.log("it worked");
            }else{
                console.log("error posting message");
            }
        });
    }

    addMessageToChat(message: string, b: boolean) {
        var li = document.createElement("li");

            li.style.cssText = 'style:none; color:red; align-items: start;border:1px;border-radius:25px;';
            var t = document.createTextNode(this.app.user.name + " " + message);

        li.appendChild(t);
        var theDiv = document.getElementById("myUL").appendChild(li);
    }
    connect(userId:string){
        var host = document.location.host;
        var messageTo;
        this.ws = new WebSocket("ws://" + host + "/comment/" + userId);
        this.ws.onmessage = function(event) {
            console.log(event.data.length);
            var message = JSON.parse(event.data);
            console.log(event.data);
            console.log(message.comment);
            if(message.comment != ""){
                console.log("CREATE ELEMENT");
                var li = document.createElement("li");
                li.style.cssText = 'color:blue;style:none;border:1px solid; border-radius:25px;';
                var t = document.createTextNode(message.comment);
                li.appendChild(t);
                var theDiv = document.getElementById("myUL").appendChild(li);
            }
        };
    }

    ping() {
        this.ws.send("asf");
    }

}
