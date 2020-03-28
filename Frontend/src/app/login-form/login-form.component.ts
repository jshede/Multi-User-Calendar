import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder } from '@angular/forms';

import { AuthenticationService } from '../backend/services';
import { AppService } from '../app.service';

@Component({
    selector: 'app-login-form',
    templateUrl: './login-form.component.html',
})
export class LoginFormComponent implements OnInit {
    loginForm = this.fb.group({
        username: ['', Validators.required],
        password: ['', Validators.required],
    });

    // Service injection
    constructor(
        private fb: FormBuilder,
        private server: AuthenticationService,
        private app: AppService,
    ) {}

    ngOnInit() {}

    loginUser() {
        const { username, password } = this.loginForm.value;

        if (this.loginForm.invalid) {
            alert('Please fill in all fields');
            return;
        }

        this.server.login({ username, password })
            .subscribe(resp => { if (resp.ok) {
                this.app.triggerLogin();
            }});
    }
}
