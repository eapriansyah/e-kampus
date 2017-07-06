import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Lecture} from './lecture.model';
import {LecturePopupService} from './lecture-popup.service';
import {LectureService} from './lecture.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-lecture-dialog',
    templateUrl: './lecture-dialog.component.html'
})
export class LectureDialogComponent implements OnInit {

    lecture: Lecture;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private lectureService: LectureService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.lecture.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.lectureService.update(this.lecture), false);
        } else {
            this.subscribeToSaveResponse(
                this.lectureService.create(this.lecture), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Lecture>, isCreated: boolean) {
        result.subscribe((res: Lecture) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Lecture, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.lecture.created'
            : 'kampusApp.lecture.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Lecture di simpan !');
        this.eventManager.broadcast({ name: 'lectureListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-lecture-popup',
    template: ''
})
export class LecturePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private lecturePopupService: LecturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.lecturePopupService
                    .open(LectureDialogComponent, params['id']);
            } else {
                this.modalRef = this.lecturePopupService
                    .open(LectureDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
