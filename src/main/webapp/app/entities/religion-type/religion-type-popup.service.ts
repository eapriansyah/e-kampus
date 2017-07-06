import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ReligionType } from './religion-type.model';
import { ReligionTypeService } from './religion-type.service';

@Injectable()
export class ReligionTypePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private religionTypeService: ReligionTypeService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.religionTypeService.find(id).subscribe((religionType) => {
                this.religionTypeModalRef(component, religionType);
            });
        } else {
            return this.religionTypeModalRef(component, new ReligionType());
        }
    }

    religionTypeModalRef(component: Component, religionType: ReligionType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.religionType = religionType;
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
