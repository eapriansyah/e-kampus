import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PreStudent} from './pre-student.model';
import {PreStudentPopupService} from './pre-student-popup.service';
import {PreStudentService} from './pre-student.service';
import {ToasterService} from '../../shared';
import { ProgramStudy, ProgramStudyService } from '../program-study';
import { StudyPath, StudyPathService } from '../study-path';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-pre-student-dialog',
    templateUrl: './pre-student-dialog.component.html'
})
export class PreStudentDialogComponent implements OnInit {

    preStudent: PreStudent;
    authorities: any[];
    isSaving: boolean;

    programstudies: ProgramStudy[];

    studypaths: StudyPath[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private preStudentService: PreStudentService,
        private programStudyService: ProgramStudyService,
        private studyPathService: StudyPathService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.programStudyService.query()
            .subscribe((res: ResponseWrapper) => { this.programstudies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studyPathService.query()
            .subscribe((res: ResponseWrapper) => { this.studypaths = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.preStudent.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.preStudentService.update(this.preStudent), false);
        } else {
            this.subscribeToSaveResponse(
                this.preStudentService.create(this.preStudent), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PreStudent>, isCreated: boolean) {
        result.subscribe((res: PreStudent) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PreStudent, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.preStudent.created'
            : 'kampusApp.preStudent.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PreStudent di simpan !');
        this.eventManager.broadcast({ name: 'preStudentListModification', content: 'OK'});
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

    trackProgramStudyById(index: number, item: ProgramStudy) {
        return item.idPartyRole;
    }

    trackStudyPathById(index: number, item: StudyPath) {
        return item.idStudyPath;
    }
}

@Component({
    selector: 'jhi-pre-student-popup',
    template: ''
})
export class PreStudentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private preStudentPopupService: PreStudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.preStudentPopupService
                    .open(PreStudentDialogComponent, params['id']);
            } else {
                this.modalRef = this.preStudentPopupService
                    .open(PreStudentDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
