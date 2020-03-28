import { Injectable } from '@angular/core';
import {
    HttpClient,
    HttpRequest,
    HttpParams,
    HttpResponse,
    HttpErrorResponse,
} from '@angular/common/http';
import { of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class ServerService {
    constructor(private http: HttpClient) {}

    // Send GET request to server
    get<T>(endpoint: string, data = {}): Observable<HttpResponse<T>> {
        const url = '/api/' + endpoint;

        return this.http.get<T>(url, {
            params: new HttpParams({ fromObject: data }),
            observe: 'response',
        }).pipe(
            catchError(this.handleError()),
        );
    }

    get2<T>(endpoint: string, data = {}): Observable<HttpResponse<T>> {
        const url = endpoint;
        return this.http.get<T>(url, {
            params: new HttpParams({ fromObject: data }),
            observe: 'response',
        }).pipe(
            catchError(this.handleError()),
        );
    }
    // Send POST request to server
    post<T>(endpoint: string, data = {}): Observable<HttpResponse<T>> {
        const url = '/api/' + endpoint;

        return this.http.post<T>(url, data, {
            observe: 'response',
        }).pipe(
            catchError(this.handleError()),
        );
    }

    private handleError() {
        return (err: HttpErrorResponse, caught) => {
            // Indicate failiure
            console.error(err);

            // Let Observable keep running
            return of(caught);
        };
    }
}
