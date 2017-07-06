import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { CourseLecture } from './course-lecture.model';
import { CourseLectureService } from './course-lecture.service';

@Injectable()
export class CourseLecturePopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private courseLectureService: CourseLectureService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.courseLectureService.find(id).subscribe((courseLecture) => {
                courseLecture.dateFrom = this.datePipe
                    .transform(courseLecture.dateFrom, 'yyyy-MM-ddThh:mm');
                courseLecture.dateThru = this.datePipe
                    .transform(courseLecture.dateThru, 'yyyy-MM-ddThh:mm');
                this.courseLectureModalRef(component, courseLecture);
            });
        } else {
            return this.courseLectureModalRef(component, new CourseLecture());
        }
    }

    courseLectureModalRef(component: Component, courseLecture: CourseLecture): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.courseLecture = courseLecture;
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
