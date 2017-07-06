import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ClassStudy } from './class-study.model';
import { ClassStudyService } from './class-study.service';

@Injectable()
export class ClassStudyPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private classStudyService: ClassStudyService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.classStudyService.find(id).subscribe((classStudy) => {
                this.classStudyModalRef(component, classStudy);
            });
        } else {
            return this.classStudyModalRef(component, new ClassStudy());
        }
    }

    classStudyModalRef(component: Component, classStudy: ClassStudy): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.classStudy = classStudy;
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
