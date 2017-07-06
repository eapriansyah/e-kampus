import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {EventAction} from './event-action.model';
import {EventActionPopupService} from './event-action-popup.service';
import {EventActionService} from './event-action.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-event-action-dialog',
    templateUrl: './event-action-dialog.component.html'
})
export class EventActionDialogComponent implements OnInit {

    eventAction: EventAction;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventActionService: EventActionService,
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
        if (this.eventAction.idEventAction !== undefined) {
            this.subscribeToSaveResponse(
                this.eventActionService.update(this.eventAction), false);
        } else {
            this.subscribeToSaveResponse(
                this.eventActionService.create(this.eventAction), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<EventAction>, isCreated: boolean) {
        result.subscribe((res: EventAction) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: EventAction, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.eventAction.created'
            : 'kampusApp.eventAction.updated',
            { param : result.idEventAction }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data EventAction di simpan !');
        this.eventManager.broadcast({ name: 'eventActionListModification', content: 'OK'});
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
    selector: 'jhi-event-action-popup',
    template: ''
})
export class EventActionPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private eventActionPopupService: EventActionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.eventActionPopupService
                    .open(EventActionDialogComponent, params['id']);
            } else {
                this.modalRef = this.eventActionPopupService
                    .open(EventActionDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
