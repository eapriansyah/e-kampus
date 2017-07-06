import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PostalAddress } from './postal-address.model';
import { PostalAddressService } from './postal-address.service';

@Injectable()
export class PostalAddressPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private postalAddressService: PostalAddressService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.postalAddressService.find(id).subscribe((postalAddress) => {
                this.postalAddressModalRef(component, postalAddress);
            });
        } else {
            return this.postalAddressModalRef(component, new PostalAddress());
        }
    }

    postalAddressModalRef(component: Component, postalAddress: PostalAddress): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.postalAddress = postalAddress;
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
