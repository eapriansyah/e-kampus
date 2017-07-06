import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ElectronicAddress} from './electronic-address.model';
import {ElectronicAddressPopupService} from './electronic-address-popup.service';
import {ElectronicAddressService} from './electronic-address.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-electronic-address-dialog',
    templateUrl: './electronic-address-dialog.component.html'
})
export class ElectronicAddressDialogComponent implements OnInit {

    electronicAddress: ElectronicAddress;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private electronicAddressService: ElectronicAddressService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.electronicAddress.idContact !== undefined) {
            this.subscribeToSaveResponse(
                this.electronicAddressService.update(this.electronicAddress), false);
        } else {
            this.subscribeToSaveResponse(
                this.electronicAddressService.create(this.electronicAddress), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ElectronicAddress>, isCreated: boolean) {
        result.subscribe((res: ElectronicAddress) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ElectronicAddress, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.electronicAddress.created'
            : 'kampusApp.electronicAddress.updated',
            { param : result.idContact }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ElectronicAddress di simpan !');
        this.eventManager.broadcast({ name: 'electronicAddressListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-electronic-address-popup',
    template: ''
})
export class ElectronicAddressPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private electronicAddressPopupService: ElectronicAddressPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.electronicAddressPopupService
                    .open(ElectronicAddressDialogComponent, params['id']);
            } else {
                this.modalRef = this.electronicAddressPopupService
                    .open(ElectronicAddressDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
