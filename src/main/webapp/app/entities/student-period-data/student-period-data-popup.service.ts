import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { StudentPeriodData } from './student-period-data.model';
import { StudentPeriodDataService } from './student-period-data.service';

@Injectable()
export class StudentPeriodDataPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private studentPeriodDataService: StudentPeriodDataService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.studentPeriodDataService.find(id).subscribe((studentPeriodData) => {
                this.studentPeriodDataModalRef(component, studentPeriodData);
            });
        } else {
            return this.studentPeriodDataModalRef(component, new StudentPeriodData());
        }
    }

    studentPeriodDataModalRef(component: Component, studentPeriodData: StudentPeriodData): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.studentPeriodData = studentPeriodData;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
