import { Injectable } from '@angular/core';
import { ServerService } from './server.service';
import { User, LoginData, SignupData } from '../objects';

@Injectable({
    providedIn: 'root',
})
export class AuthenticationService {
    constructor(private server: ServerService) {}

    login(loginData: LoginData) {
        return this.server.post('login', loginData);
    }

    logout() {
        return this.server.get('logout');
    }

    signup(signupData: SignupData) {
        // TODO: Hash the password before sending
        return this.server.post('register', signupData);
    }

    getCurrentUser() {
        return this.server.get<User>('getCurrentUser');
    }

    getUserId() {
        return this.server.get<any>('getUserId');
    }
}
