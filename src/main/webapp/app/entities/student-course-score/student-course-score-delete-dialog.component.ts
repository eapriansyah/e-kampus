import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudentCourseScore} from './student-course-score.model';
import {StudentCourseScorePopupService} from './student-course-score-popup.service';
import {StudentCourseScoreService} from './student-course-score.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-course-score-delete-dialog',
    templateUrl: './student-course-score-delete-dialog.component.html'
})
export class StudentCourseScoreDeleteDialogComponent {

    studentCourseScore: StudentCourseScore;

    constructor(
        private studentCourseScoreService: StudentCourseScoreService,
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
        this.studentCourseScoreService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudentCourseScore di hapus !');
            this.eventManager.broadcast({
                name: 'studentCourseScoreListModification',
                content: 'Deleted an studentCourseScore'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studentCourseScore.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-course-score-delete-popup',
    template: ''
})
export class StudentCourseScoreDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentCourseScorePopupService: StudentCourseScorePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentCourseScorePopupService
                .open(StudentCourseScoreDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
