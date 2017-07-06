import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudentEvent} from './student-event.model';
import {StudentEventPopupService} from './student-event-popup.service';
import {StudentEventService} from './student-event.service';
import {ToasterService} from '../../shared';
import { EventAction, EventActionService } from '../event-action';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-event-dialog',
    templateUrl: './student-event-dialog.component.html'
})
export class StudentEventDialogComponent implements OnInit {

    studentEvent: StudentEvent;
    authorities: any[];
    isSaving: boolean;

    eventactions: EventAction[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentEventService: StudentEventService,
        private eventActionService: EventActionService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.eventActionService.query()
            .subscribe((res: ResponseWrapper) => { this.eventactions = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.studentEvent.idStudentEvent !== undefined) {
            this.subscribeToSaveResponse(
                this.studentEventService.update(this.studentEvent), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentEventService.create(this.studentEvent), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentEvent>, isCreated: boolean) {
        result.subscribe((res: StudentEvent) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentEvent, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studentEvent.created'
            : 'kampusApp.studentEvent.updated',
            { param : result.idStudentEvent }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudentEvent di simpan !');
        this.eventManager.broadcast({ name: 'studentEventListModification', content: 'OK'});
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

    trackEventActionById(index: number, item: EventAction) {
        return item.idEventAction;
    }
}

@Component({
    selector: 'jhi-student-event-popup',
    template: ''
})
export class StudentEventPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentEventPopupService: StudentEventPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentEventPopupService
                    .open(StudentEventDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentEventPopupService
                    .open(StudentEventDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
