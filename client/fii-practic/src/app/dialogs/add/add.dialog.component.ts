import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { DoctorService } from 'src/app/services/doctor.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnackBarService } from 'src/app/services/snackbar.service';
import { Doctor } from 'src/app/models/doctor';

@Component({
    selector: 'app-doctor-add.dialog',
    templateUrl: './add.dialog.component.html',
    styleUrls: ['./add.dialog.component.css']
})
export class AddDoctorDialogComponent {

    form: FormGroup;

    constructor(public dialogRef: MatDialogRef<AddDoctorDialogComponent>,
                @Inject(MAT_DIALOG_DATA) public data,
                private formBuilder: FormBuilder,
                private doctorService: DoctorService,
                private snackBarService: SnackBarService) {
        this.createForm();
    }

    private createForm() {
        this.form = this.formBuilder.group({
            firstName: ['', Validators.required],
            lastName: ['', Validators.required],
            speciality: [''],
            email: ['', Validators.email],
            phone: ['', Validators.pattern('\\+04\\d{10}')]
        });
    }

    createDoctor() {
        const formModel = this.form.value;
        const doctor: Doctor = {
            firstName: formModel.firstName as string,
            lastName: formModel.lastName as string,
            speciality: formModel.speciality as string,
            email: {
                email: formModel.email as string
            },
            phone: {
                phoneNumber: formModel.phone as string
            }
        };
        this.doctorService.addDoctor(doctor).subscribe(res => {
            this.snackBarService.showSnackBar('Doctor was successfully added');
            this.dialogRef.close(res);
        }, err => {
            this.snackBarService.showSnackBar('Could not add doctor');
        });
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}
