import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Zone} from './zone.model';
import {ZonePopupService} from './zone-popup.service';
import {ZoneService} from './zone.service';
import {ToasterService} from '../../shared';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-zone-dialog',
    templateUrl: './zone-dialog.component.html'
})
export class ZoneDialogComponent implements OnInit {

    zone: Zone;
    authorities: any[];
    isSaving: boolean;

    locations: Location[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private zoneService: ZoneService,
        private locationService: LocationService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.zone.idGeoBoundary !== undefined) {
            this.subscribeToSaveResponse(
                this.zoneService.update(this.zone), false);
        } else {
            this.subscribeToSaveResponse(
                this.zoneService.create(this.zone), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Zone>, isCreated: boolean) {
        result.subscribe((res: Zone) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Zone, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.zone.created'
            : 'kampusApp.zone.updated',
            { param : result.idGeoBoundary }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Zone di simpan !');
        this.eventManager.broadcast({ name: 'zoneListModification', content: 'OK'});
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

    trackLocationById(index: number, item: Location) {
        return item.idGeoBoundary;
    }
}

@Component({
    selector: 'jhi-zone-popup',
    template: ''
})
export class ZonePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private zonePopupService: ZonePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.zonePopupService
                    .open(ZoneDialogComponent, params['id']);
            } else {
                this.modalRef = this.zonePopupService
                    .open(ZoneDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
