import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PersonalData } from './personal-data.model';
import { PersonalDataService } from './personal-data.service';

@Injectable()
export class PersonalDataPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private personalDataService: PersonalDataService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.personalDataService.find(id).subscribe((personalData) => {
                if (personalData.fatherDob) {
                    personalData.fatherDob = {
                        year: personalData.fatherDob.getFullYear(),
                        month: personalData.fatherDob.getMonth() + 1,
                        day: personalData.fatherDob.getDate()
                    };
                }
                if (personalData.motherDob) {
                    personalData.motherDob = {
                        year: personalData.motherDob.getFullYear(),
                        month: personalData.motherDob.getMonth() + 1,
                        day: personalData.motherDob.getDate()
                    };
                }
                this.personalDataModalRef(component, personalData);
            });
        } else {
            return this.personalDataModalRef(component, new PersonalData());
        }
    }

    personalDataModalRef(component: Component, personalData: PersonalData): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.personalData = personalData;
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
