import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './/app-routing.module';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginFormComponent } from './login-form/login-form.component';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { EventFormComponent } from './event-form/event-form.component';
import { GroupFormComponent } from './group-form/group-form.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CalendarComponent } from './calendar/calendar.component';
import { CalendarDayCellComponent } from './calendar/day-cell/day-cell.component';
import { CalendarEventComponent } from './calendar/day-cell/event/event.component';
import { CalendarControlsComponent } from './calendar/controls/controls.component';
import { CalendarSidebarComponent } from './calendar/sidebar/sidebar.component';
import { MonthViewComponent } from './calendar/month-view/month-view.component';
import { WeekViewComponent } from './calendar/week-view/week-view.component';
import { DayViewComponent } from './calendar/day-view/day-view.component';
import { EventViewComponent } from './event-view/event-view.component';

import { AuthenticationService, GroupService, RoleService, EventService } from './backend/services';
import { AppService } from './app.service';
import { CalendarService } from './calendar/calendar.service';
import { EventViewService } from './event-view/event-view.service';

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        FooterComponent,
        LoginFormComponent,
        HomepageComponent,
        SignupFormComponent,
        CalendarComponent,
        CalendarDayCellComponent,
        CalendarEventComponent,
        CalendarControlsComponent,
        CalendarSidebarComponent,
        MonthViewComponent,
        WeekViewComponent,
        DayViewComponent,
        GroupFormComponent,
        EventFormComponent,
        EventViewComponent,
    ],
    imports: [
        BrowserModule,
        NgbModule.forRoot(),
        AppRoutingModule,
        ReactiveFormsModule,
        HttpClientModule,
    ],
    providers: [
        AuthenticationService,
        EventService,
        RoleService,
        GroupService,
        AppService,
        CalendarService,
        EventViewService,
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
