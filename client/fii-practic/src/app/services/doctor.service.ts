import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { APP_CONSTANTS } from '../app.constants';
import { Observable } from 'rxjs';
import { Doctor } from '../models/doctor';

@Injectable()
export class DoctorService {
    constructor(private httpClient: HttpClient) {}

    public getDoctors(): Observable<Doctor[]> {
        return this.httpClient.get<Doctor[]>(`${APP_CONSTANTS.ENDPOINT}/api/doctors`);
    }

    public deleteDoctor(id: number) {
        return this.httpClient.delete(`${APP_CONSTANTS.ENDPOINT}/api/doctors/${id}`);
    }

    public addDoctor(doctor: Doctor) {
        return this.httpClient.post(`${APP_CONSTANTS.ENDPOINT}/api/doctors`, doctor);
    }
}
