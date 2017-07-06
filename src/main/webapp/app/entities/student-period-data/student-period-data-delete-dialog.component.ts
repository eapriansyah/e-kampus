import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudentPeriodData} from './student-period-data.model';
import {StudentPeriodDataPopupService} from './student-period-data-popup.service';
import {StudentPeriodDataService} from './student-period-data.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-period-data-delete-dialog',
    templateUrl: './student-period-data-delete-dialog.component.html'
})
export class StudentPeriodDataDeleteDialogComponent {

    studentPeriodData: StudentPeriodData;

    constructor(
        private studentPeriodDataService: StudentPeriodDataService,
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
        this.studentPeriodDataService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudentPeriodData di hapus !');
            this.eventManager.broadcast({
                name: 'studentPeriodDataListModification',
                content: 'Deleted an studentPeriodData'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studentPeriodData.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-period-data-delete-popup',
    template: ''
})
export class StudentPeriodDataDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPeriodDataPopupService: StudentPeriodDataPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentPeriodDataPopupService
                .open(StudentPeriodDataDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
