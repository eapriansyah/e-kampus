import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ElectronicAddress } from './electronic-address.model';
import { ElectronicAddressService } from './electronic-address.service';

@Injectable()
export class ElectronicAddressPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private electronicAddressService: ElectronicAddressService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.electronicAddressService.find(id).subscribe((electronicAddress) => {
                this.electronicAddressModalRef(component, electronicAddress);
            });
        } else {
            return this.electronicAddressModalRef(component, new ElectronicAddress());
        }
    }

    electronicAddressModalRef(component: Component, electronicAddress: ElectronicAddress): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.electronicAddress = electronicAddress;
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
