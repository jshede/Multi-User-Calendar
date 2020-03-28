import { Component, OnInit, Input } from '@angular/core';
import { AuthenticationService } from '../backend/services';

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit {
    title = "Koalendar";
    @Input() username: string;

    constructor(private server : AuthenticationService) {}

    ngOnInit() {}
}
