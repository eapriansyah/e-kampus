import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ReligionType} from './religion-type.model';
import {ReligionTypePopupService} from './religion-type-popup.service';
import {ReligionTypeService} from './religion-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-religion-type-dialog',
    templateUrl: './religion-type-dialog.component.html'
})
export class ReligionTypeDialogComponent implements OnInit {

    religionType: ReligionType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private religionTypeService: ReligionTypeService,
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
        if (this.religionType.idReligionType !== undefined) {
            this.subscribeToSaveResponse(
                this.religionTypeService.update(this.religionType), false);
        } else {
            this.subscribeToSaveResponse(
                this.religionTypeService.create(this.religionType), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ReligionType>, isCreated: boolean) {
        result.subscribe((res: ReligionType) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ReligionType, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.religionType.created'
            : 'kampusApp.religionType.updated',
            { param : result.idReligionType }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ReligionType di simpan !');
        this.eventManager.broadcast({ name: 'religionTypeListModification', content: 'OK'});
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
    selector: 'jhi-religion-type-popup',
    template: ''
})
export class ReligionTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private religionTypePopupService: ReligionTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.religionTypePopupService
                    .open(ReligionTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.religionTypePopupService
                    .open(ReligionTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
