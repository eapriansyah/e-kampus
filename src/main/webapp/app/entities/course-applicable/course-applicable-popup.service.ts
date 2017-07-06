import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { CourseApplicable } from './course-applicable.model';
import { CourseApplicableService } from './course-applicable.service';

@Injectable()
export class CourseApplicablePopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private courseApplicableService: CourseApplicableService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.courseApplicableService.find(id).subscribe((courseApplicable) => {
                courseApplicable.dateFrom = this.datePipe
                    .transform(courseApplicable.dateFrom, 'yyyy-MM-ddThh:mm');
                courseApplicable.dateThru = this.datePipe
                    .transform(courseApplicable.dateThru, 'yyyy-MM-ddThh:mm');
                this.courseApplicableModalRef(component, courseApplicable);
            });
        } else {
            return this.courseApplicableModalRef(component, new CourseApplicable());
        }
    }

    courseApplicableModalRef(component: Component, courseApplicable: CourseApplicable): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.courseApplicable = courseApplicable;
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
