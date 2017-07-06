import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PostalAddress} from './postal-address.model';
import {PostalAddressPopupService} from './postal-address-popup.service';
import {PostalAddressService} from './postal-address.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-postal-address-delete-dialog',
    templateUrl: './postal-address-delete-dialog.component.html'
})
export class PostalAddressDeleteDialogComponent {

    postalAddress: PostalAddress;

    constructor(
        private postalAddressService: PostalAddressService,
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
        this.postalAddressService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PostalAddress di hapus !');
            this.eventManager.broadcast({
                name: 'postalAddressListModification',
                content: 'Deleted an postalAddress'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.postalAddress.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-postal-address-delete-popup',
    template: ''
})
export class PostalAddressDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postalAddressPopupService: PostalAddressPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.postalAddressPopupService
                .open(PostalAddressDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
