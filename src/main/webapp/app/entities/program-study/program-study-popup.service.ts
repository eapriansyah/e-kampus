import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ProgramStudy } from './program-study.model';
import { ProgramStudyService } from './program-study.service';

@Injectable()
export class ProgramStudyPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private programStudyService: ProgramStudyService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.programStudyService.find(id).subscribe((programStudy) => {
                this.programStudyModalRef(component, programStudy);
            });
        } else {
            return this.programStudyModalRef(component, new ProgramStudy());
        }
    }

    programStudyModalRef(component: Component, programStudy: ProgramStudy): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.programStudy = programStudy;
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
