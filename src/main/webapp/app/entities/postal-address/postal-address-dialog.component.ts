import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PostalAddress} from './postal-address.model';
import {PostalAddressPopupService} from './postal-address-popup.service';
import {PostalAddressService} from './postal-address.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-postal-address-dialog',
    templateUrl: './postal-address-dialog.component.html'
})
export class PostalAddressDialogComponent implements OnInit {

    postalAddress: PostalAddress;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private postalAddressService: PostalAddressService,
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
        if (this.postalAddress.idContact !== undefined) {
            this.subscribeToSaveResponse(
                this.postalAddressService.update(this.postalAddress), false);
        } else {
            this.subscribeToSaveResponse(
                this.postalAddressService.create(this.postalAddress), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PostalAddress>, isCreated: boolean) {
        result.subscribe((res: PostalAddress) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PostalAddress, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.postalAddress.created'
            : 'kampusApp.postalAddress.updated',
            { param : result.idContact }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PostalAddress di simpan !');
        this.eventManager.broadcast({ name: 'postalAddressListModification', content: 'OK'});
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
    selector: 'jhi-postal-address-popup',
    template: ''
})
export class PostalAddressPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postalAddressPopupService: PostalAddressPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.postalAddressPopupService
                    .open(PostalAddressDialogComponent, params['id']);
            } else {
                this.modalRef = this.postalAddressPopupService
                    .open(PostalAddressDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
