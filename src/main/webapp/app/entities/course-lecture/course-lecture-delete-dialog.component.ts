import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {CourseLecture} from './course-lecture.model';
import {CourseLecturePopupService} from './course-lecture-popup.service';
import {CourseLectureService} from './course-lecture.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-course-lecture-delete-dialog',
    templateUrl: './course-lecture-delete-dialog.component.html'
})
export class CourseLectureDeleteDialogComponent {

    courseLecture: CourseLecture;

    constructor(
        private courseLectureService: CourseLectureService,
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
        this.courseLectureService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data CourseLecture di hapus !');
            this.eventManager.broadcast({
                name: 'courseLectureListModification',
                content: 'Deleted an courseLecture'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.courseLecture.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-course-lecture-delete-popup',
    template: ''
})
export class CourseLectureDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courseLecturePopupService: CourseLecturePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.courseLecturePopupService
                .open(CourseLectureDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
