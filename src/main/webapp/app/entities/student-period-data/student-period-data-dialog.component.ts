import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudentPeriodData} from './student-period-data.model';
import {StudentPeriodDataPopupService} from './student-period-data-popup.service';
import {StudentPeriodDataService} from './student-period-data.service';
import {ToasterService} from '../../shared';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { Course, CourseService } from '../course';
import { Student, StudentService } from '../student';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-period-data-dialog',
    templateUrl: './student-period-data-dialog.component.html'
})
export class StudentPeriodDataDialogComponent implements OnInit {

    studentPeriodData: StudentPeriodData;
    authorities: any[];
    isSaving: boolean;

    academicperiods: AcademicPeriods[];

    courses: Course[];

    students: Student[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentPeriodDataService: StudentPeriodDataService,
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
        if (this.studentPeriodData.idStudentPeriod !== undefined) {
            this.subscribeToSaveResponse(
                this.studentPeriodDataService.update(this.studentPeriodData), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentPeriodDataService.create(this.studentPeriodData), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentPeriodData>, isCreated: boolean) {
        result.subscribe((res: StudentPeriodData) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentPeriodData, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studentPeriodData.created'
            : 'kampusApp.studentPeriodData.updated',
            { param : result.idStudentPeriod }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudentPeriodData di simpan !');
        this.eventManager.broadcast({ name: 'studentPeriodDataListModification', content: 'OK'});
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
    selector: 'jhi-student-period-data-popup',
    template: ''
})
export class StudentPeriodDataPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPeriodDataPopupService: StudentPeriodDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentPeriodDataPopupService
                    .open(StudentPeriodDataDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentPeriodDataPopupService
                    .open(StudentPeriodDataDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
