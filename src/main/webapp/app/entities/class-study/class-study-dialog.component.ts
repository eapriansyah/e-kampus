import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ClassStudy} from './class-study.model';
import {ClassStudyPopupService} from './class-study-popup.service';
import {ClassStudyService} from './class-study.service';
import {ToasterService} from '../../shared';
import { Course, CourseService } from '../course';
import { ProgramStudy, ProgramStudyService } from '../program-study';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { Lecture, LectureService } from '../lecture';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-class-study-dialog',
    templateUrl: './class-study-dialog.component.html'
})
export class ClassStudyDialogComponent implements OnInit {

    classStudy: ClassStudy;
    authorities: any[];
    isSaving: boolean;

    courses: Course[];

    programstudies: ProgramStudy[];

    academicperiods: AcademicPeriods[];

    lectures: Lecture[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private classStudyService: ClassStudyService,
        private courseService: CourseService,
        private programStudyService: ProgramStudyService,
        private academicPeriodsService: AcademicPeriodsService,
        private lectureService: LectureService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.programStudyService.query()
            .subscribe((res: ResponseWrapper) => { this.programstudies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.academicPeriodsService.query()
            .subscribe((res: ResponseWrapper) => { this.academicperiods = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.lectureService.query()
            .subscribe((res: ResponseWrapper) => { this.lectures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.classStudy.idClassStudy !== undefined) {
            this.subscribeToSaveResponse(
                this.classStudyService.update(this.classStudy), false);
        } else {
            this.subscribeToSaveResponse(
                this.classStudyService.create(this.classStudy), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ClassStudy>, isCreated: boolean) {
        result.subscribe((res: ClassStudy) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ClassStudy, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.classStudy.created'
            : 'kampusApp.classStudy.updated',
            { param : result.idClassStudy }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ClassStudy di simpan !');
        this.eventManager.broadcast({ name: 'classStudyListModification', content: 'OK'});
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

    trackCourseById(index: number, item: Course) {
        return item.idCourse;
    }

    trackProgramStudyById(index: number, item: ProgramStudy) {
        return item.idPartyRole;
    }

    trackAcademicPeriodsById(index: number, item: AcademicPeriods) {
        return item.idPeriod;
    }

    trackLectureById(index: number, item: Lecture) {
        return item.idPartyRole;
    }
}

@Component({
    selector: 'jhi-class-study-popup',
    template: ''
})
export class ClassStudyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private classStudyPopupService: ClassStudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.classStudyPopupService
                    .open(ClassStudyDialogComponent, params['id']);
            } else {
                this.modalRef = this.classStudyPopupService
                    .open(ClassStudyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
