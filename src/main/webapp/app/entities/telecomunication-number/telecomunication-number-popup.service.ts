import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TelecomunicationNumber } from './telecomunication-number.model';
import { TelecomunicationNumberService } from './telecomunication-number.service';

@Injectable()
export class TelecomunicationNumberPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private telecomunicationNumberService: TelecomunicationNumberService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.telecomunicationNumberService.find(id).subscribe((telecomunicationNumber) => {
                this.telecomunicationNumberModalRef(component, telecomunicationNumber);
            });
        } else {
            return this.telecomunicationNumberModalRef(component, new TelecomunicationNumber());
        }
    }

    telecomunicationNumberModalRef(component: Component, telecomunicationNumber: TelecomunicationNumber): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.telecomunicationNumber = telecomunicationNumber;
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
