import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ExtraCourse } from './extra-course.model';
import { ExtraCourseService } from './extra-course.service';

@Injectable()
export class ExtraCoursePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private extraCourseService: ExtraCourseService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.extraCourseService.find(id).subscribe((extraCourse) => {
                this.extraCourseModalRef(component, extraCourse);
            });
        } else {
            return this.extraCourseModalRef(component, new ExtraCourse());
        }
    }

    extraCourseModalRef(component: Component, extraCourse: ExtraCourse): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.extraCourse = extraCourse;
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
