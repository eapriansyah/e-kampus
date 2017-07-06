import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Building} from './building.model';
import {BuildingPopupService} from './building-popup.service';
import {BuildingService} from './building.service';
import {ToasterService} from '../../shared';
import { Zone, ZoneService } from '../zone';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-building-dialog',
    templateUrl: './building-dialog.component.html'
})
export class BuildingDialogComponent implements OnInit {

    building: Building;
    authorities: any[];
    isSaving: boolean;

    zones: Zone[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private buildingService: BuildingService,
        private zoneService: ZoneService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.zoneService.query()
            .subscribe((res: ResponseWrapper) => { this.zones = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.building.idFacility !== undefined) {
            this.subscribeToSaveResponse(
                this.buildingService.update(this.building), false);
        } else {
            this.subscribeToSaveResponse(
                this.buildingService.create(this.building), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Building>, isCreated: boolean) {
        result.subscribe((res: Building) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Building, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.building.created'
            : 'kampusApp.building.updated',
            { param : result.idFacility }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Building di simpan !');
        this.eventManager.broadcast({ name: 'buildingListModification', content: 'OK'});
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

    trackZoneById(index: number, item: Zone) {
        return item.idGeoBoundary;
    }
}

@Component({
    selector: 'jhi-building-popup',
    template: ''
})
export class BuildingPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private buildingPopupService: BuildingPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.buildingPopupService
                    .open(BuildingDialogComponent, params['id']);
            } else {
                this.modalRef = this.buildingPopupService
                    .open(BuildingDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
