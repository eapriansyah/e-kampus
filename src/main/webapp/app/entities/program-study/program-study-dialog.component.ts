import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ProgramStudy} from './program-study.model';
import {ProgramStudyPopupService} from './program-study-popup.service';
import {ProgramStudyService} from './program-study.service';
import {ToasterService} from '../../shared';
import { Degree, DegreeService } from '../degree';
import { Faculty, FacultyService } from '../faculty';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-program-study-dialog',
    templateUrl: './program-study-dialog.component.html'
})
export class ProgramStudyDialogComponent implements OnInit {

    programStudy: ProgramStudy;
    authorities: any[];
    isSaving: boolean;

    degrees: Degree[];

    faculties: Faculty[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private programStudyService: ProgramStudyService,
        private DegreeService: DegreeService,
        private FacultyService: FacultyService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.DegreeService.query()
            .subscribe((res: ResponseWrapper) => { this.degrees = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.FacultyService.query()
            .subscribe((res: ResponseWrapper) => { this.faculties = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.programStudy.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.programStudyService.update(this.programStudy), false);
        } else {
            this.subscribeToSaveResponse(
                this.programStudyService.create(this.programStudy), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ProgramStudy>, isCreated: boolean) {
        result.subscribe((res: ProgramStudy) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ProgramStudy, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.programStudy.created'
            : 'kampusApp.programStudy.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ProgramStudy di simpan !');
        this.eventManager.broadcast({ name: 'programStudyListModification', content: 'OK'});
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

    trackDegreeById(index: number, item: Degree) {
        return item.idDegree;
    }

    trackFacultyById(index: number, item: Faculty) {
        return item.idPartyRole;
    }
}

@Component({
    selector: 'jhi-program-study-popup',
    template: ''
})
export class ProgramStudyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private programStudyPopupService: ProgramStudyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.programStudyPopupService
                    .open(ProgramStudyDialogComponent, params['id']);
            } else {
                this.modalRef = this.programStudyPopupService
                    .open(ProgramStudyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
