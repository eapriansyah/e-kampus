import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {CourseApplicable} from './course-applicable.model';
import {CourseApplicablePopupService} from './course-applicable-popup.service';
import {CourseApplicableService} from './course-applicable.service';
import {ToasterService} from '../../shared';
import { ProgramStudy, ProgramStudyService } from '../program-study';
import { Course, CourseService } from '../course';
import { PeriodType, PeriodTypeService } from '../period-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-course-applicable-dialog',
    templateUrl: './course-applicable-dialog.component.html'
})
export class CourseApplicableDialogComponent implements OnInit {

    courseApplicable: CourseApplicable;
    authorities: any[];
    isSaving: boolean;

    programstudies: ProgramStudy[];

    courses: Course[];

    periodtypes: PeriodType[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private courseApplicableService: CourseApplicableService,
        private programStudyService: ProgramStudyService,
        private courseService: CourseService,
        private periodTypeService: PeriodTypeService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.programStudyService.query()
            .subscribe((res: ResponseWrapper) => { this.programstudies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.periodTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.periodtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.courseApplicable.idApplCourse !== undefined) {
            this.subscribeToSaveResponse(
                this.courseApplicableService.update(this.courseApplicable), false);
        } else {
            this.subscribeToSaveResponse(
                this.courseApplicableService.create(this.courseApplicable), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<CourseApplicable>, isCreated: boolean) {
        result.subscribe((res: CourseApplicable) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CourseApplicable, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.courseApplicable.created'
            : 'kampusApp.courseApplicable.updated',
            { param : result.idApplCourse }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data CourseApplicable di simpan !');
        this.eventManager.broadcast({ name: 'courseApplicableListModification', content: 'OK'});
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

    trackCourseById(index: number, item: Course) {
        return item.idCourse;
    }

    trackPeriodTypeById(index: number, item: PeriodType) {
        return item.idPeriodType;
    }
}

@Component({
    selector: 'jhi-course-applicable-popup',
    template: ''
})
export class CourseApplicablePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courseApplicablePopupService: CourseApplicablePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.courseApplicablePopupService
                    .open(CourseApplicableDialogComponent, params['id']);
            } else {
                this.modalRef = this.courseApplicablePopupService
                    .open(CourseApplicableDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
