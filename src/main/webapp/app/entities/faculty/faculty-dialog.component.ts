import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Faculty} from './faculty.model';
import {FacultyPopupService} from './faculty-popup.service';
import {FacultyService} from './faculty.service';
import {ToasterService} from '../../shared';
import { University, UniversityService } from '../university';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-faculty-dialog',
    templateUrl: './faculty-dialog.component.html'
})
export class FacultyDialogComponent implements OnInit {

    faculty: Faculty;
    authorities: any[];
    isSaving: boolean;

    universities: University[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private facultyService: FacultyService,
        private universityService: UniversityService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.universityService.query()
            .subscribe((res: ResponseWrapper) => { this.universities = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.faculty.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.facultyService.update(this.faculty), false);
        } else {
            this.subscribeToSaveResponse(
                this.facultyService.create(this.faculty), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Faculty>, isCreated: boolean) {
        result.subscribe((res: Faculty) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Faculty, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.faculty.created'
            : 'kampusApp.faculty.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Faculty di simpan !');
        this.eventManager.broadcast({ name: 'facultyListModification', content: 'OK'});
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

    trackUniversityById(index: number, item: University) {
        return item.idPartyRole;
    }
}

@Component({
    selector: 'jhi-faculty-popup',
    template: ''
})
export class FacultyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private facultyPopupService: FacultyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.facultyPopupService
                    .open(FacultyDialogComponent, params['id']);
            } else {
                this.modalRef = this.facultyPopupService
                    .open(FacultyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
