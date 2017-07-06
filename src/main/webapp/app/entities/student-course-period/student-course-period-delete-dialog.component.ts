import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudentCoursePeriod} from './student-course-period.model';
import {StudentCoursePeriodPopupService} from './student-course-period-popup.service';
import {StudentCoursePeriodService} from './student-course-period.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-course-period-delete-dialog',
    templateUrl: './student-course-period-delete-dialog.component.html'
})
export class StudentCoursePeriodDeleteDialogComponent {

    studentCoursePeriod: StudentCoursePeriod;

    constructor(
        private studentCoursePeriodService: StudentCoursePeriodService,
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
        this.studentCoursePeriodService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudentCoursePeriod di hapus !');
            this.eventManager.broadcast({
                name: 'studentCoursePeriodListModification',
                content: 'Deleted an studentCoursePeriod'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studentCoursePeriod.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-course-period-delete-popup',
    template: ''
})
export class StudentCoursePeriodDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentCoursePeriodPopupService: StudentCoursePeriodPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentCoursePeriodPopupService
                .open(StudentCoursePeriodDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
