import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { Event } from './backend/objects';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { CalendarComponent } from './calendar/calendar.component';
import { EventFormComponent } from './event-form/event-form.component';
import { GroupFormComponent } from './group-form/group-form.component';
import { EventViewComponent } from './event-view/event-view.component';

const routes: Routes = [
    { path: '', component: HomepageComponent },
    { path: 'login', component: LoginFormComponent },
    { path: 'signup', component: SignupFormComponent },
    { path: 'calendar', component: CalendarComponent },
    { path: 'new-event', component: EventFormComponent },
    { path: 'new-group', component: GroupFormComponent },
    { path: 'event-view', component: EventViewComponent },
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ],
})
export class AppRoutingModule {}
