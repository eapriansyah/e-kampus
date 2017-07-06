import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PeriodType} from './period-type.model';
import {PeriodTypePopupService} from './period-type-popup.service';
import {PeriodTypeService} from './period-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-period-type-dialog',
    templateUrl: './period-type-dialog.component.html'
})
export class PeriodTypeDialogComponent implements OnInit {

    periodType: PeriodType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private periodTypeService: PeriodTypeService,
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
        if (this.periodType.idPeriodType !== undefined) {
            this.subscribeToSaveResponse(
                this.periodTypeService.update(this.periodType), false);
        } else {
            this.subscribeToSaveResponse(
                this.periodTypeService.create(this.periodType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PeriodType>, isCreated: boolean) {
        result.subscribe((res: PeriodType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PeriodType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.periodType.created'
            : 'kampusApp.periodType.updated',
            { param : result.idPeriodType }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PeriodType di simpan !');
        this.eventManager.broadcast({ name: 'periodTypeListModification', content: 'OK'});
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
    selector: 'jhi-period-type-popup',
    template: ''
})
export class PeriodTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private periodTypePopupService: PeriodTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.periodTypePopupService
                    .open(PeriodTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.periodTypePopupService
                    .open(PeriodTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
