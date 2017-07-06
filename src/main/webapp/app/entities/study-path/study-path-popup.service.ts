import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { StudyPath } from './study-path.model';
import { StudyPathService } from './study-path.service';

@Injectable()
export class StudyPathPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private studyPathService: StudyPathService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.studyPathService.find(id).subscribe((studyPath) => {
                this.studyPathModalRef(component, studyPath);
            });
        } else {
            return this.studyPathModalRef(component, new StudyPath());
        }
    }

    studyPathModalRef(component: Component, studyPath: StudyPath): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.studyPath = studyPath;
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
