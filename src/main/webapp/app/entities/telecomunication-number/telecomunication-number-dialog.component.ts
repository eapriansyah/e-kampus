import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {TelecomunicationNumber} from './telecomunication-number.model';
import {TelecomunicationNumberPopupService} from './telecomunication-number-popup.service';
import {TelecomunicationNumberService} from './telecomunication-number.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-telecomunication-number-dialog',
    templateUrl: './telecomunication-number-dialog.component.html'
})
export class TelecomunicationNumberDialogComponent implements OnInit {

    telecomunicationNumber: TelecomunicationNumber;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private telecomunicationNumberService: TelecomunicationNumberService,
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
        if (this.telecomunicationNumber.idContact !== undefined) {
            this.subscribeToSaveResponse(
                this.telecomunicationNumberService.update(this.telecomunicationNumber), false);
        } else {
            this.subscribeToSaveResponse(
                this.telecomunicationNumberService.create(this.telecomunicationNumber), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<TelecomunicationNumber>, isCreated: boolean) {
        result.subscribe((res: TelecomunicationNumber) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TelecomunicationNumber, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.telecomunicationNumber.created'
            : 'kampusApp.telecomunicationNumber.updated',
            { param : result.idContact }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data TelecomunicationNumber di simpan !');
        this.eventManager.broadcast({ name: 'telecomunicationNumberListModification', content: 'OK'});
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
    selector: 'jhi-telecomunication-number-popup',
    template: ''
})
export class TelecomunicationNumberPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private telecomunicationNumberPopupService: TelecomunicationNumberPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.telecomunicationNumberPopupService
                    .open(TelecomunicationNumberDialogComponent, params['id']);
            } else {
                this.modalRef = this.telecomunicationNumberPopupService
                    .open(TelecomunicationNumberDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
