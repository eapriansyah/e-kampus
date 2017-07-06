import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { StudentCourseScore } from './student-course-score.model';
import { StudentCourseScoreService } from './student-course-score.service';

@Injectable()
export class StudentCourseScorePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private studentCourseScoreService: StudentCourseScoreService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.studentCourseScoreService.find(id).subscribe((studentCourseScore) => {
                this.studentCourseScoreModalRef(component, studentCourseScore);
            });
        } else {
            return this.studentCourseScoreModalRef(component, new StudentCourseScore());
        }
    }

    studentCourseScoreModalRef(component: Component, studentCourseScore: StudentCourseScore): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.studentCourseScore = studentCourseScore;
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
