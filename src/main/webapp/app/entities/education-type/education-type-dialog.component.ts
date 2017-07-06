import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {EducationType} from './education-type.model';
import {EducationTypePopupService} from './education-type-popup.service';
import {EducationTypeService} from './education-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-education-type-dialog',
    templateUrl: './education-type-dialog.component.html'
})
export class EducationTypeDialogComponent implements OnInit {

    educationType: EducationType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private educationTypeService: EducationTypeService,
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
        if (this.educationType.idEducationType !== undefined) {
            this.subscribeToSaveResponse(
                this.educationTypeService.update(this.educationType), false);
        } else {
            this.subscribeToSaveResponse(
                this.educationTypeService.create(this.educationType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<EducationType>, isCreated: boolean) {
        result.subscribe((res: EducationType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: EducationType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.educationType.created'
            : 'kampusApp.educationType.updated',
            { param : result.idEducationType }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data EducationType di simpan !');
        this.eventManager.broadcast({ name: 'educationTypeListModification', content: 'OK'});
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
    selector: 'jhi-education-type-popup',
    template: ''
})
export class EducationTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationTypePopupService: EducationTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.educationTypePopupService
                    .open(EducationTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.educationTypePopupService
                    .open(EducationTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
