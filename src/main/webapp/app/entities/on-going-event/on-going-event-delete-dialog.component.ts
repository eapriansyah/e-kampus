import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {OnGoingEvent} from './on-going-event.model';
import {OnGoingEventPopupService} from './on-going-event-popup.service';
import {OnGoingEventService} from './on-going-event.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-on-going-event-delete-dialog',
    templateUrl: './on-going-event-delete-dialog.component.html'
})
export class OnGoingEventDeleteDialogComponent {

    onGoingEvent: OnGoingEvent;

    constructor(
        private onGoingEventService: OnGoingEventService,
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
        this.onGoingEventService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data OnGoingEvent di hapus !');
            this.eventManager.broadcast({
                name: 'onGoingEventListModification',
                content: 'Deleted an onGoingEvent'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.onGoingEvent.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-on-going-event-delete-popup',
    template: ''
})
export class OnGoingEventDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private onGoingEventPopupService: OnGoingEventPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.onGoingEventPopupService
                .open(OnGoingEventDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
