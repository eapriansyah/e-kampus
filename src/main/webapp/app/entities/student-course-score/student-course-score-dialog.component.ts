import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudentCourseScore} from './student-course-score.model';
import {StudentCourseScorePopupService} from './student-course-score-popup.service';
import {StudentCourseScoreService} from './student-course-score.service';
import {ToasterService} from '../../shared';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { Course, CourseService } from '../course';
import { Student, StudentService } from '../student';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-course-score-dialog',
    templateUrl: './student-course-score-dialog.component.html'
})
export class StudentCourseScoreDialogComponent implements OnInit {

    studentCourseScore: StudentCourseScore;
    authorities: any[];
    isSaving: boolean;

    academicperiods: AcademicPeriods[];

    courses: Course[];

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentCourseScoreService: StudentCourseScoreService,
        private academicPeriodsService: AcademicPeriodsService,
        private courseService: CourseService,
        private studentService: StudentService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.academicPeriodsService.query()
            .subscribe((res: ResponseWrapper) => { this.academicperiods = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studentService.query()
            .subscribe((res: ResponseWrapper) => { this.students = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.studentCourseScore.idStudentCourseScore !== undefined) {
            this.subscribeToSaveResponse(
                this.studentCourseScoreService.update(this.studentCourseScore), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentCourseScoreService.create(this.studentCourseScore), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentCourseScore>, isCreated: boolean) {
        result.subscribe((res: StudentCourseScore) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentCourseScore, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studentCourseScore.created'
            : 'kampusApp.studentCourseScore.updated',
            { param : result.idStudentCourseScore }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudentCourseScore di simpan !');
        this.eventManager.broadcast({ name: 'studentCourseScoreListModification', content: 'OK'});
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

    trackAcademicPeriodsById(index: number, item: AcademicPeriods) {
        return item.idPeriod;
    }

    trackCourseById(index: number, item: Course) {
        return item.idCourse;
    }

    trackStudentById(index: number, item: Student) {
        return item.idPartyRole;
    }
}

@Component({
    selector: 'jhi-student-course-score-popup',
    template: ''
})
export class StudentCourseScorePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentCourseScorePopupService: StudentCourseScorePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentCourseScorePopupService
                    .open(StudentCourseScoreDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentCourseScorePopupService
                    .open(StudentCourseScoreDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
