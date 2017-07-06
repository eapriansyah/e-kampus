import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Lab} from './lab.model';
import {LabPopupService} from './lab-popup.service';
import {LabService} from './lab.service';
import {ToasterService} from '../../shared';
import { Building, BuildingService } from '../building';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-lab-dialog',
    templateUrl: './lab-dialog.component.html'
})
export class LabDialogComponent implements OnInit {

    lab: Lab;
    authorities: any[];
    isSaving: boolean;

    buildings: Building[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private labService: LabService,
        private buildingService: BuildingService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.buildingService.query()
            .subscribe((res: ResponseWrapper) => { this.buildings = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.lab.idFacility !== undefined) {
            this.subscribeToSaveResponse(
                this.labService.update(this.lab), false);
        } else {
            this.subscribeToSaveResponse(
                this.labService.create(this.lab), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Lab>, isCreated: boolean) {
        result.subscribe((res: Lab) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Lab, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.lab.created'
            : 'kampusApp.lab.updated',
            { param : result.idFacility }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Lab di simpan !');
        this.eventManager.broadcast({ name: 'labListModification', content: 'OK'});
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

    trackBuildingById(index: number, item: Building) {
        return item.idFacility;
    }
}

@Component({
    selector: 'jhi-lab-popup',
    template: ''
})
export class LabPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private labPopupService: LabPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.labPopupService
                    .open(LabDialogComponent, params['id']);
            } else {
                this.modalRef = this.labPopupService
                    .open(LabDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
