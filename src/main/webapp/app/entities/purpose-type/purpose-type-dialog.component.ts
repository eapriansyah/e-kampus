import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PurposeType} from './purpose-type.model';
import {PurposeTypePopupService} from './purpose-type-popup.service';
import {PurposeTypeService} from './purpose-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-purpose-type-dialog',
    templateUrl: './purpose-type-dialog.component.html'
})
export class PurposeTypeDialogComponent implements OnInit {

    purposeType: PurposeType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private purposeTypeService: PurposeTypeService,
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
        if (this.purposeType.idPurposeType !== undefined) {
            this.subscribeToSaveResponse(
                this.purposeTypeService.update(this.purposeType), false);
        } else {
            this.subscribeToSaveResponse(
                this.purposeTypeService.create(this.purposeType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PurposeType>, isCreated: boolean) {
        result.subscribe((res: PurposeType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PurposeType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.purposeType.created'
            : 'kampusApp.purposeType.updated',
            { param : result.idPurposeType }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PurposeType di simpan !');
        this.eventManager.broadcast({ name: 'purposeTypeListModification', content: 'OK'});
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
    selector: 'jhi-purpose-type-popup',
    template: ''
})
export class PurposeTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purposeTypePopupService: PurposeTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.purposeTypePopupService
                    .open(PurposeTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.purposeTypePopupService
                    .open(PurposeTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
