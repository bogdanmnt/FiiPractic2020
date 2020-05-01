import { NgModule } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatInputModule } from '@angular/material/input';
import { MatSortModule } from '@angular/material/sort';

@NgModule({
    imports: [
        MatInputModule,
        MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        MatSnackBarModule,
        MatTableModule,
        MatPaginatorModule,
        MatDialogModule,
        MatSortModule
    ],
    exports: [
        MatInputModule,
        MatButtonModule,
        MatToolbarModule,
        MatIconModule,
        MatSnackBarModule,
        MatTableModule,
        MatPaginatorModule,
        MatDialogModule,
        MatSortModule
    ]})
export class MaterialModule {

}
