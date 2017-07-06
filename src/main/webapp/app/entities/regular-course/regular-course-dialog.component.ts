import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {RegularCourse} from './regular-course.model';
import {RegularCoursePopupService} from './regular-course-popup.service';
import {RegularCourseService} from './regular-course.service';
import {ToasterService} from '../../shared';
import { Internal, InternalService } from '../internal';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-regular-course-dialog',
    templateUrl: './regular-course-dialog.component.html'
})
export class RegularCourseDialogComponent implements OnInit {

    regularCourse: RegularCourse;
    authorities: any[];
    isSaving: boolean;

    internals: Internal[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private regularCourseService: RegularCourseService,
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
        if (this.regularCourse.idCourse !== undefined) {
            this.subscribeToSaveResponse(
                this.regularCourseService.update(this.regularCourse), false);
        } else {
            this.subscribeToSaveResponse(
                this.regularCourseService.create(this.regularCourse), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<RegularCourse>, isCreated: boolean) {
        result.subscribe((res: RegularCourse) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: RegularCourse, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.regularCourse.created'
            : 'kampusApp.regularCourse.updated',
            { param : result.idCourse }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data RegularCourse di simpan !');
        this.eventManager.broadcast({ name: 'regularCourseListModification', content: 'OK'});
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
    selector: 'jhi-regular-course-popup',
    template: ''
})
export class RegularCoursePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private regularCoursePopupService: RegularCoursePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.regularCoursePopupService
                    .open(RegularCourseDialogComponent, params['id']);
            } else {
                this.modalRef = this.regularCoursePopupService
                    .open(RegularCourseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
