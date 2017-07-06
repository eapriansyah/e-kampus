import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Student} from './student.model';
import {StudentPopupService} from './student-popup.service';
import {StudentService} from './student.service';
import {ToasterService} from '../../shared';
import { ProgramStudy, ProgramStudyService } from '../program-study';
import { StudyPath, StudyPathService } from '../study-path';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-dialog',
    templateUrl: './student-dialog.component.html'
})
export class StudentDialogComponent implements OnInit {

    student: Student;
    authorities: any[];
    isSaving: boolean;

    programstudies: ProgramStudy[];

    studypaths: StudyPath[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentService: StudentService,
        private ProgramStudyService: ProgramStudyService,
        private StudyPathService: StudyPathService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.ProgramStudyService.query()
            .subscribe((res: ResponseWrapper) => { this.programstudies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.StudyPathService.query()
            .subscribe((res: ResponseWrapper) => { this.studypaths = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.student.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.studentService.update(this.student), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentService.create(this.student), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Student>, isCreated: boolean) {
        result.subscribe((res: Student) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Student, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.student.created'
            : 'kampusApp.student.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Student di simpan !');
        this.eventManager.broadcast({ name: 'studentListModification', content: 'OK'});
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
    selector: 'jhi-student-popup',
    template: ''
})
export class StudentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPopupService: StudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentPopupService
                    .open(StudentDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentPopupService
                    .open(StudentDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
