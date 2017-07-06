import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { ContactMechanismPurpose } from './contact-mechanism-purpose.model';
import { ContactMechanismPurposeService } from './contact-mechanism-purpose.service';

@Injectable()
export class ContactMechanismPurposePopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private contactMechanismPurposeService: ContactMechanismPurposeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.contactMechanismPurposeService.find(id).subscribe((contactMechanismPurpose) => {
                contactMechanismPurpose.dateFrom = this.datePipe
                    .transform(contactMechanismPurpose.dateFrom, 'yyyy-MM-ddThh:mm');
                contactMechanismPurpose.dateThru = this.datePipe
                    .transform(contactMechanismPurpose.dateThru, 'yyyy-MM-ddThh:mm');
                this.contactMechanismPurposeModalRef(component, contactMechanismPurpose);
            });
        } else {
            return this.contactMechanismPurposeModalRef(component, new ContactMechanismPurpose());
        }
    }

    contactMechanismPurposeModalRef(component: Component, contactMechanismPurpose: ContactMechanismPurpose): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contactMechanismPurpose = contactMechanismPurpose;
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
