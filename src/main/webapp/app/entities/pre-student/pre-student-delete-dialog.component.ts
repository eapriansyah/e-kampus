import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PreStudent} from './pre-student.model';
import {PreStudentPopupService} from './pre-student-popup.service';
import {PreStudentService} from './pre-student.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-pre-student-delete-dialog',
    templateUrl: './pre-student-delete-dialog.component.html'
})
export class PreStudentDeleteDialogComponent {

    preStudent: PreStudent;

    constructor(
        private preStudentService: PreStudentService,
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
        this.preStudentService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PreStudent di hapus !');
            this.eventManager.broadcast({
                name: 'preStudentListModification',
                content: 'Deleted an preStudent'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.preStudent.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-pre-student-delete-popup',
    template: ''
})
export class PreStudentDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private preStudentPopupService: PreStudentPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.preStudentPopupService
                .open(PreStudentDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
