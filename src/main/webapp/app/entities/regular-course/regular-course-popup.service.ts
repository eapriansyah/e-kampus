import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { RegularCourse } from './regular-course.model';
import { RegularCourseService } from './regular-course.service';

@Injectable()
export class RegularCoursePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private regularCourseService: RegularCourseService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.regularCourseService.find(id).subscribe((regularCourse) => {
                this.regularCourseModalRef(component, regularCourse);
            });
        } else {
            return this.regularCourseModalRef(component, new RegularCourse());
        }
    }

    regularCourseModalRef(component: Component, regularCourse: RegularCourse): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.regularCourse = regularCourse;
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
