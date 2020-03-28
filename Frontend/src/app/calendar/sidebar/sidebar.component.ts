import { Component, OnInit, OnDestroy, HostBinding } from '@angular/core';
import { Subscription } from 'rxjs/index';

import { AppService } from '../../app.service';
import { CalendarService } from '../calendar.service';
import { Group } from '../../backend/objects';

@Component({
    selector: 'app-calendar-sidebar',
    templateUrl: './sidebar.component.html',
})
export class CalendarSidebarComponent implements OnInit, OnDestroy {
    groups: Group[];
    showGroup: boolean[];
    
    @HostBinding('class.collapsed')
    collapsed: boolean;

    private subs: Subscription[];
    
    constructor(
        private app: AppService,
        private calendar: CalendarService,
    ) {
        this.groups = [];
        this.showGroup = [];
        this.collapsed = false;
        
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
        let oldShowGroup = this.showGroup;
        
        this.groups = this.app.groups;
        this.showGroup = [];
        
        for (let group of this.groups) {
            let id = group.id;
            if (oldShowGroup[id] != null) {
                this.showGroup[id] = oldShowGroup[id];
            } else {
                this.showGroup[id] = true;
            }
        }
    }

    toggleCollapse() {
        this.collapsed = !this.collapsed;
    }

    toggleGroup(group: Group) {
        this.showGroup[group.id] = !this.showGroup[group.id];
        this.calendar.triggerToggleGroup(group);
    }
}
