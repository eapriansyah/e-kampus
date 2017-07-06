import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ElectronicAddress} from './electronic-address.model';
import {ElectronicAddressPopupService} from './electronic-address-popup.service';
import {ElectronicAddressService} from './electronic-address.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-electronic-address-delete-dialog',
    templateUrl: './electronic-address-delete-dialog.component.html'
})
export class ElectronicAddressDeleteDialogComponent {

    electronicAddress: ElectronicAddress;

    constructor(
        private electronicAddressService: ElectronicAddressService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id?: any) {
        this.electronicAddressService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ElectronicAddress di hapus !');
            this.eventManager.broadcast({
                name: 'electronicAddressListModification',
                content: 'Deleted an electronicAddress'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.electronicAddress.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-electronic-address-delete-popup',
    template: ''
})
export class ElectronicAddressDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private electronicAddressPopupService: ElectronicAddressPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.electronicAddressPopupService
                .open(ElectronicAddressDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
