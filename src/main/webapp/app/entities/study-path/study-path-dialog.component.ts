import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudyPath} from './study-path.model';
import {StudyPathPopupService} from './study-path-popup.service';
import {StudyPathService} from './study-path.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-study-path-dialog',
    templateUrl: './study-path-dialog.component.html'
})
export class StudyPathDialogComponent implements OnInit {

    studyPath: StudyPath;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studyPathService: StudyPathService,
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
        if (this.studyPath.idStudyPath !== undefined) {
            this.subscribeToSaveResponse(
                this.studyPathService.update(this.studyPath), false);
        } else {
            this.subscribeToSaveResponse(
                this.studyPathService.create(this.studyPath), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudyPath>, isCreated: boolean) {
        result.subscribe((res: StudyPath) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudyPath, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studyPath.created'
            : 'kampusApp.studyPath.updated',
            { param : result.idStudyPath }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudyPath di simpan !');
        this.eventManager.broadcast({ name: 'studyPathListModification', content: 'OK'});
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
    selector: 'jhi-study-path-popup',
    template: ''
})
export class StudyPathPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studyPathPopupService: StudyPathPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studyPathPopupService
                    .open(StudyPathDialogComponent, params['id']);
            } else {
                this.modalRef = this.studyPathPopupService
                    .open(StudyPathDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
