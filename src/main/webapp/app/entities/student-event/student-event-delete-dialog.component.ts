import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudentEvent} from './student-event.model';
import {StudentEventPopupService} from './student-event-popup.service';
import {StudentEventService} from './student-event.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-event-delete-dialog',
    templateUrl: './student-event-delete-dialog.component.html'
})
export class StudentEventDeleteDialogComponent {

    studentEvent: StudentEvent;

    constructor(
        private studentEventService: StudentEventService,
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
        this.studentEventService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudentEvent di hapus !');
            this.eventManager.broadcast({
                name: 'studentEventListModification',
                content: 'Deleted an studentEvent'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studentEvent.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-event-delete-popup',
    template: ''
})
export class StudentEventDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentEventPopupService: StudentEventPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentEventPopupService
                .open(StudentEventDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
