import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {OnGoingEvent} from './on-going-event.model';
import {OnGoingEventPopupService} from './on-going-event-popup.service';
import {OnGoingEventService} from './on-going-event.service';
import {ToasterService} from '../../shared';
import { Internal, InternalService } from '../internal';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { StudentEvent, StudentEventService } from '../student-event';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-on-going-event-dialog',
    templateUrl: './on-going-event-dialog.component.html'
})
export class OnGoingEventDialogComponent implements OnInit {

    onGoingEvent: OnGoingEvent;
    authorities: any[];
    isSaving: boolean;

    internals: Internal[];

    academicperiods: AcademicPeriods[];

    studentevents: StudentEvent[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private onGoingEventService: OnGoingEventService,
        private internalService: InternalService,
        private academicPeriodsService: AcademicPeriodsService,
        private studentEventService: StudentEventService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.internalService.query()
            .subscribe((res: ResponseWrapper) => { this.internals = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.academicPeriodsService.query()
            .subscribe((res: ResponseWrapper) => { this.academicperiods = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studentEventService.query()
            .subscribe((res: ResponseWrapper) => { this.studentevents = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.onGoingEvent.idEventGo !== undefined) {
            this.subscribeToSaveResponse(
                this.onGoingEventService.update(this.onGoingEvent), false);
        } else {
            this.subscribeToSaveResponse(
                this.onGoingEventService.create(this.onGoingEvent), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<OnGoingEvent>, isCreated: boolean) {
        result.subscribe((res: OnGoingEvent) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: OnGoingEvent, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.onGoingEvent.created'
            : 'kampusApp.onGoingEvent.updated',
            { param : result.idEventGo }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data OnGoingEvent di simpan !');
        this.eventManager.broadcast({ name: 'onGoingEventListModification', content: 'OK'});
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

    trackInternalById(index: number, item: Internal) {
        return item.idPartyRole;
    }

    trackAcademicPeriodsById(index: number, item: AcademicPeriods) {
        return item.idPeriod;
    }

    trackStudentEventById(index: number, item: StudentEvent) {
        return item.idStudentEvent;
    }
}

@Component({
    selector: 'jhi-on-going-event-popup',
    template: ''
})
export class OnGoingEventPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private onGoingEventPopupService: OnGoingEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.onGoingEventPopupService
                    .open(OnGoingEventDialogComponent, params['id']);
            } else {
                this.modalRef = this.onGoingEventPopupService
                    .open(OnGoingEventDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
