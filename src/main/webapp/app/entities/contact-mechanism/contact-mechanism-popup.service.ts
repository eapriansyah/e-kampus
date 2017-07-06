import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ContactMechanism } from './contact-mechanism.model';
import { ContactMechanismService } from './contact-mechanism.service';

@Injectable()
export class ContactMechanismPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private contactMechanismService: ContactMechanismService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.contactMechanismService.find(id).subscribe((contactMechanism) => {
                this.contactMechanismModalRef(component, contactMechanism);
            });
        } else {
            return this.contactMechanismModalRef(component, new ContactMechanism());
        }
    }

    contactMechanismModalRef(component: Component, contactMechanism: ContactMechanism): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contactMechanism = contactMechanism;
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
