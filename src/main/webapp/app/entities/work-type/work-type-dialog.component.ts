import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {WorkType} from './work-type.model';
import {WorkTypePopupService} from './work-type-popup.service';
import {WorkTypeService} from './work-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-work-type-dialog',
    templateUrl: './work-type-dialog.component.html'
})
export class WorkTypeDialogComponent implements OnInit {

    workType: WorkType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private workTypeService: WorkTypeService,
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
        if (this.workType.idWorkType !== undefined) {
            this.subscribeToSaveResponse(
                this.workTypeService.update(this.workType), false);
        } else {
            this.subscribeToSaveResponse(
                this.workTypeService.create(this.workType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<WorkType>, isCreated: boolean) {
        result.subscribe((res: WorkType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: WorkType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.workType.created'
            : 'kampusApp.workType.updated',
            { param : result.idWorkType }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data WorkType di simpan !');
        this.eventManager.broadcast({ name: 'workTypeListModification', content: 'OK'});
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
    selector: 'jhi-work-type-popup',
    template: ''
})
export class WorkTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workTypePopupService: WorkTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.workTypePopupService
                    .open(WorkTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.workTypePopupService
                    .open(WorkTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
