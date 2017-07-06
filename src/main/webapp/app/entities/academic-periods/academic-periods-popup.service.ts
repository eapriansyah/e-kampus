import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { AcademicPeriods } from './academic-periods.model';
import { AcademicPeriodsService } from './academic-periods.service';

@Injectable()
export class AcademicPeriodsPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private academicPeriodsService: AcademicPeriodsService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.academicPeriodsService.find(id).subscribe((academicPeriods) => {
                academicPeriods.dateFrom = this.datePipe
                    .transform(academicPeriods.dateFrom, 'yyyy-MM-ddThh:mm');
                academicPeriods.dateThru = this.datePipe
                    .transform(academicPeriods.dateThru, 'yyyy-MM-ddThh:mm');
                this.academicPeriodsModalRef(component, academicPeriods);
            });
        } else {
            return this.academicPeriodsModalRef(component, new AcademicPeriods());
        }
    }

    academicPeriodsModalRef(component: Component, academicPeriods: AcademicPeriods): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.academicPeriods = academicPeriods;
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
