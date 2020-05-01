import { Component, OnInit, ViewChild, AfterViewInit, ElementRef } from '@angular/core';
import { DoctorService } from './services/doctor.service';
import { MatTableDataSource, MatPaginator, MatDialog, MatSort, Sort, MatSortable } from '@angular/material';
import { Doctor } from './models/doctor';
import { SnackBarService } from './services/snackbar.service';
import { AddDoctorDialogComponent } from './dialogs/add/add.dialog.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'fii-practic';
  displayedColumns = ['Id', 'FirstName', 'LastName', 'Speciality', 'Email', 'Actions'];

  dataSource: MatTableDataSource<Doctor>;
  @ViewChild(MatPaginator,  {static: false}) paginator: MatPaginator;
  @ViewChild(MatSort, { static: false }) sort: MatSort;

  constructor(private doctorService: DoctorService,
              private snackBarService: SnackBarService,
              private dialog: MatDialog,
              ) {
  }

  ngOnInit() {
    this.doctorService.getDoctors().subscribe(doctors => {
      this.dataSource = new MatTableDataSource(doctors);
      console.log(doctors);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    }, err => {
      this.snackBarService.showSnackBar('Could not load doctors');
      console.log(err);
    });
  }

  deleteDoctor(doctor: Doctor) {
    this.doctorService.deleteDoctor(doctor.id).subscribe(res => {
      this.snackBarService.showSnackBar('Doctor was sucessfully deleted');

      const index = this.dataSource.data.indexOf(doctor);
      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription();
    }, err => {
      this.snackBarService.showSnackBar('Doctor could not be deleted');
      console.log(err);
    });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  addNew() {
    const dialogRef = this.dialog.open(AddDoctorDialogComponent, { disableClose: true });
    dialogRef.afterClosed().subscribe(result => {
      this.dataSource.data.push(result);
      this.dataSource._updateChangeSubscription();
    });
  }

  sortData(sortState: Sort) {
    if (!sortState.active || sortState.direction === '') {
      return;
    }

    const data = this.dataSource.data;
    this.dataSource.data = data.sort((a, b) => {
      const isAsc = sortState.direction === 'asc';
      switch (sortState.active) {
        case 'Id': return this.compare(a.id, b.id, isAsc);
        case 'FirstName': return this.compare(a.firstName, b.firstName, isAsc);
        case 'LastName': return this.compare(a.lastName, b.lastName, isAsc);
        case 'Speciality': return this.compare(a.speciality, b.speciality, isAsc);
        default: return 0;
      }
    });
  }

  private compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

}
