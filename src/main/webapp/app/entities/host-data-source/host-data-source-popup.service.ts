import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HostDataSource } from './host-data-source.model';
import { HostDataSourceService } from './host-data-source.service';

@Injectable()
export class HostDataSourcePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private hostDataSourceService: HostDataSourceService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.hostDataSourceService.find(id).subscribe((hostDataSource) => {
                this.hostDataSourceModalRef(component, hostDataSource);
            });
        } else {
            return this.hostDataSourceModalRef(component, new HostDataSource());
        }
    }

    hostDataSourceModalRef(component: Component, hostDataSource: HostDataSource): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.hostDataSource = hostDataSource;
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
