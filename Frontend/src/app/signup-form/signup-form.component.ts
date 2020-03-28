import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { AppService } from '../app.service';
import { AuthenticationService } from '../backend/services';
import { SignupData } from '../backend/objects';

@Component({
    selector: 'app-signup-form',
    templateUrl: './signup-form.component.html',
})
export class SignupFormComponent implements OnInit {
    failed = false;

    form = this.fb.group({
        name:            ['', Validators.required],
        username:        ['', Validators.required],
        email:           ['', Validators.compose([Validators.required, Validators.email])],
        password:        ['', Validators.compose([Validators.required, Validators.minLength(8)])],
        confirmPassword: ['', Validators.compose([Validators.required, Validators.minLength(8)])],
    }, { validator: this.passwordMatchValidator });

    passwordMatchValidator(g: FormGroup) {
        if (g.get('password').value === g.get('confirmPassword').value) {
            return null;
        } else {
            return { mismatch: true };
        }
    }

    constructor(
        private fb: FormBuilder,
        private authentication: AuthenticationService,
        private app: AppService,
    ) {}

    ngOnInit() {}

    submit() {
        // Check form validation
        if (this.form.invalid) {
            this.failed = true;
            return;
        }

        // Extract form values
        const signupData: SignupData = this.form.value;

        // Register with server
        this.authentication.signup(signupData)
            .subscribe(() => this.onSignup(signupData));
    }

    onSignup(signupData: SignupData) {
        this.app.triggerSignup(signupData);
    }
}
