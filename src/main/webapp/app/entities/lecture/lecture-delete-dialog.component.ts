import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Lecture} from './lecture.model';
import {LecturePopupService} from './lecture-popup.service';
import {LectureService} from './lecture.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-lecture-delete-dialog',
    templateUrl: './lecture-delete-dialog.component.html'
})
export class LectureDeleteDialogComponent {

    lecture: Lecture;

    constructor(
        private lectureService: LectureService,
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
        this.lectureService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Lecture di hapus !');
            this.eventManager.broadcast({
                name: 'lectureListModification',
                content: 'Deleted an lecture'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.lecture.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-lecture-delete-popup',
    template: ''
})
export class LectureDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lecturePopupService: LecturePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.lecturePopupService
                .open(LectureDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
