import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Course} from './course.model';
import {CoursePopupService} from './course-popup.service';
import {CourseService} from './course.service';
import {ToasterService} from '../../shared';
import { Internal, InternalService } from '../internal';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-course-dialog',
    templateUrl: './course-dialog.component.html'
})
export class CourseDialogComponent implements OnInit {

    course: Course;
    authorities: any[];
    isSaving: boolean;

    internals: Internal[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private courseService: CourseService,
        private internalService: InternalService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.internalService.query()
            .subscribe((res: ResponseWrapper) => { this.internals = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.course.idCourse !== undefined) {
            this.subscribeToSaveResponse(
                this.courseService.update(this.course), false);
        } else {
            this.subscribeToSaveResponse(
                this.courseService.create(this.course), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Course>, isCreated: boolean) {
        result.subscribe((res: Course) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Course, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.course.created'
            : 'kampusApp.course.updated',
            { param : result.idCourse }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Course di simpan !');
        this.eventManager.broadcast({ name: 'courseListModification', content: 'OK'});
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

    trackInternalById(index: number, item: Internal) {
        return item.idPartyRole;
    }
}

@Component({
    selector: 'jhi-course-popup',
    template: ''
})
export class CoursePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coursePopupService: CoursePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.coursePopupService
                    .open(CourseDialogComponent, params['id']);
            } else {
                this.modalRef = this.coursePopupService
                    .open(CourseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
