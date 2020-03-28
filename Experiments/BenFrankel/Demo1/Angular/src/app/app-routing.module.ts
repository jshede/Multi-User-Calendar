import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { GardenComponent } from './garden/garden.component';
import { SkyComponent } from './sky/sky.component';

const routes: Routes = [
    { path: 'garden', component: GardenComponent },
    { path: 'sky', component: SkyComponent },
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ],
})
export class AppRoutingModule { }
