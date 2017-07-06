import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PreStudent } from './pre-student.model';
import { PreStudentService } from './pre-student.service';

@Injectable()
export class PreStudentPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private preStudentService: PreStudentService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.preStudentService.find(id).subscribe((preStudent) => {
                this.preStudentModalRef(component, preStudent);
            });
        } else {
            return this.preStudentModalRef(component, new PreStudent());
        }
    }

    preStudentModalRef(component: Component, preStudent: PreStudent): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.preStudent = preStudent;
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
