import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudentPeriods} from './student-periods.model';
import {StudentPeriodsPopupService} from './student-periods-popup.service';
import {StudentPeriodsService} from './student-periods.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-periods-delete-dialog',
    templateUrl: './student-periods-delete-dialog.component.html'
})
export class StudentPeriodsDeleteDialogComponent {

    studentPeriods: StudentPeriods;

    constructor(
        private studentPeriodsService: StudentPeriodsService,
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
        this.studentPeriodsService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudentPeriods di hapus !');
            this.eventManager.broadcast({
                name: 'studentPeriodsListModification',
                content: 'Deleted an studentPeriods'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studentPeriods.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-periods-delete-popup',
    template: ''
})
export class StudentPeriodsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPeriodsPopupService: StudentPeriodsPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentPeriodsPopupService
                .open(StudentPeriodsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
