import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {EventAction} from './event-action.model';
import {EventActionPopupService} from './event-action-popup.service';
import {EventActionService} from './event-action.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-event-action-delete-dialog',
    templateUrl: './event-action-delete-dialog.component.html'
})
export class EventActionDeleteDialogComponent {

    eventAction: EventAction;

    constructor(
        private eventActionService: EventActionService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id?: any) {
        this.eventActionService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data EventAction di hapus !');
            this.eventManager.broadcast({
                name: 'eventActionListModification',
                content: 'Deleted an eventAction'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.eventAction.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-event-action-delete-popup',
    template: ''
})
export class EventActionDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private eventActionPopupService: EventActionPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.eventActionPopupService
                .open(EventActionDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
