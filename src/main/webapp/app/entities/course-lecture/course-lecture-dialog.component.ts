import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {CourseLecture} from './course-lecture.model';
import {CourseLecturePopupService} from './course-lecture-popup.service';
import {CourseLectureService} from './course-lecture.service';
import {ToasterService} from '../../shared';
import { Lecture, LectureService } from '../lecture';
import { Course, CourseService } from '../course';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-course-lecture-dialog',
    templateUrl: './course-lecture-dialog.component.html'
})
export class CourseLectureDialogComponent implements OnInit {

    courseLecture: CourseLecture;
    authorities: any[];
    isSaving: boolean;

    lectures: Lecture[];

    courses: Course[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private courseLectureService: CourseLectureService,
        private lectureService: LectureService,
        private courseService: CourseService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.lectureService.query()
            .subscribe((res: ResponseWrapper) => { this.lectures = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.courseService.query()
            .subscribe((res: ResponseWrapper) => { this.courses = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.courseLecture.idCourseLecture !== undefined) {
            this.subscribeToSaveResponse(
                this.courseLectureService.update(this.courseLecture), false);
        } else {
            this.subscribeToSaveResponse(
                this.courseLectureService.create(this.courseLecture), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<CourseLecture>, isCreated: boolean) {
        result.subscribe((res: CourseLecture) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CourseLecture, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.courseLecture.created'
            : 'kampusApp.courseLecture.updated',
            { param : result.idCourseLecture }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data CourseLecture di simpan !');
        this.eventManager.broadcast({ name: 'courseLectureListModification', content: 'OK'});
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

    trackLectureById(index: number, item: Lecture) {
        return item.idPartyRole;
    }

    trackCourseById(index: number, item: Course) {
        return item.idCourse;
    }
}

@Component({
    selector: 'jhi-course-lecture-popup',
    template: ''
})
export class CourseLecturePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courseLecturePopupService: CourseLecturePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.courseLecturePopupService
                    .open(CourseLectureDialogComponent, params['id']);
            } else {
                this.modalRef = this.courseLecturePopupService
                    .open(CourseLectureDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
