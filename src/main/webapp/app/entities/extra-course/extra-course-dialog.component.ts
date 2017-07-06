import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ExtraCourse} from './extra-course.model';
import {ExtraCoursePopupService} from './extra-course-popup.service';
import {ExtraCourseService} from './extra-course.service';
import {ToasterService} from '../../shared';
import { Internal, InternalService } from '../internal';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-extra-course-dialog',
    templateUrl: './extra-course-dialog.component.html'
})
export class ExtraCourseDialogComponent implements OnInit {

    extraCourse: ExtraCourse;
    authorities: any[];
    isSaving: boolean;

    internals: Internal[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private extraCourseService: ExtraCourseService,
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
        if (this.extraCourse.idCourse !== undefined) {
            this.subscribeToSaveResponse(
                this.extraCourseService.update(this.extraCourse), false);
        } else {
            this.subscribeToSaveResponse(
                this.extraCourseService.create(this.extraCourse), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ExtraCourse>, isCreated: boolean) {
        result.subscribe((res: ExtraCourse) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ExtraCourse, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.extraCourse.created'
            : 'kampusApp.extraCourse.updated',
            { param : result.idCourse }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ExtraCourse di simpan !');
        this.eventManager.broadcast({ name: 'extraCourseListModification', content: 'OK'});
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
    selector: 'jhi-extra-course-popup',
    template: ''
})
export class ExtraCoursePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private extraCoursePopupService: ExtraCoursePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.extraCoursePopupService
                    .open(ExtraCourseDialogComponent, params['id']);
            } else {
                this.modalRef = this.extraCoursePopupService
                    .open(ExtraCourseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
