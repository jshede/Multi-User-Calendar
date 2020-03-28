import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { GardenComponent } from './garden/garden.component';
import { AppRoutingModule } from './/app-routing.module';
import { SkyComponent } from './sky/sky.component';

@NgModule({
    declarations: [
        AppComponent,
        GardenComponent,
        SkyComponent,
    ],
    imports: [
        BrowserModule,
        FormsModule,
        AppRoutingModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule { }
