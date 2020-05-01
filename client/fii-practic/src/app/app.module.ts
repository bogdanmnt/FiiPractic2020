import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { MaterialModule } from './material.module';
import { SnackBarService } from './services/snackbar.service';
import { DoctorService } from './services/doctor.service';
import { AddDoctorDialogComponent } from './dialogs/add/add.dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    AddDoctorDialogComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule,
    HttpClientModule
  ],
  providers: [
    SnackBarService,
    DoctorService
  ],
  entryComponents: [
    AddDoctorDialogComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
