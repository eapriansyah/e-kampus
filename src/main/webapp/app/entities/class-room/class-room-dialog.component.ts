import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ClassRoom} from './class-room.model';
import {ClassRoomPopupService} from './class-room-popup.service';
import {ClassRoomService} from './class-room.service';
import {ToasterService} from '../../shared';
import { Building, BuildingService } from '../building';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-class-room-dialog',
    templateUrl: './class-room-dialog.component.html'
})
export class ClassRoomDialogComponent implements OnInit {

    classRoom: ClassRoom;
    authorities: any[];
    isSaving: boolean;

    buildings: Building[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private classRoomService: ClassRoomService,
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
        if (this.classRoom.idFacility !== undefined) {
            this.subscribeToSaveResponse(
                this.classRoomService.update(this.classRoom), false);
        } else {
            this.subscribeToSaveResponse(
                this.classRoomService.create(this.classRoom), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ClassRoom>, isCreated: boolean) {
        result.subscribe((res: ClassRoom) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ClassRoom, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.classRoom.created'
            : 'kampusApp.classRoom.updated',
            { param : result.idFacility }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ClassRoom di simpan !');
        this.eventManager.broadcast({ name: 'classRoomListModification', content: 'OK'});
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
    selector: 'jhi-class-room-popup',
    template: ''
})
export class ClassRoomPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private classRoomPopupService: ClassRoomPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.classRoomPopupService
                    .open(ClassRoomDialogComponent, params['id']);
            } else {
                this.modalRef = this.classRoomPopupService
                    .open(ClassRoomDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
