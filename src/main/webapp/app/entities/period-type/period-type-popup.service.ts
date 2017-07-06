import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PeriodType } from './period-type.model';
import { PeriodTypeService } from './period-type.service';

@Injectable()
export class PeriodTypePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private periodTypeService: PeriodTypeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.periodTypeService.find(id).subscribe((periodType) => {
                this.periodTypeModalRef(component, periodType);
            });
        } else {
            return this.periodTypeModalRef(component, new PeriodType());
        }
    }

    periodTypeModalRef(component: Component, periodType: PeriodType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.periodType = periodType;
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
