import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudentCoursePeriod} from './student-course-period.model';
import {StudentCoursePeriodPopupService} from './student-course-period-popup.service';
import {StudentCoursePeriodService} from './student-course-period.service';
import {ToasterService} from '../../shared';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { Course, CourseService } from '../course';
import { Student, StudentService } from '../student';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-course-period-dialog',
    templateUrl: './student-course-period-dialog.component.html'
})
export class StudentCoursePeriodDialogComponent implements OnInit {

    studentCoursePeriod: StudentCoursePeriod;
    authorities: any[];
    isSaving: boolean;

    academicperiods: AcademicPeriods[];

    courses: Course[];

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentCoursePeriodService: StudentCoursePeriodService,
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
        if (this.studentCoursePeriod.idStudentCoursePeriod !== undefined) {
            this.subscribeToSaveResponse(
                this.studentCoursePeriodService.update(this.studentCoursePeriod), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentCoursePeriodService.create(this.studentCoursePeriod), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentCoursePeriod>, isCreated: boolean) {
        result.subscribe((res: StudentCoursePeriod) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentCoursePeriod, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studentCoursePeriod.created'
            : 'kampusApp.studentCoursePeriod.updated',
            { param : result.idStudentCoursePeriod }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudentCoursePeriod di simpan !');
        this.eventManager.broadcast({ name: 'studentCoursePeriodListModification', content: 'OK'});
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
    selector: 'jhi-student-course-period-popup',
    template: ''
})
export class StudentCoursePeriodPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentCoursePeriodPopupService: StudentCoursePeriodPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentCoursePeriodPopupService
                    .open(StudentCoursePeriodDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentCoursePeriodPopupService
                    .open(StudentCoursePeriodDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
