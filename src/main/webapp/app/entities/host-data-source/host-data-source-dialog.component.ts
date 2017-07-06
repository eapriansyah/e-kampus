import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {HostDataSource} from './host-data-source.model';
import {HostDataSourcePopupService} from './host-data-source-popup.service';
import {HostDataSourceService} from './host-data-source.service';
import {ToasterService} from '../../shared';
import { ProgramStudy, ProgramStudyService } from '../program-study';
import { StudyPath, StudyPathService } from '../study-path';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-host-data-source-dialog',
    templateUrl: './host-data-source-dialog.component.html'
})
export class HostDataSourceDialogComponent implements OnInit {

    hostDataSource: HostDataSource;
    authorities: any[];
    isSaving: boolean;

    programstudies: ProgramStudy[];

    studypaths: StudyPath[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private hostDataSourceService: HostDataSourceService,
        private programStudyService: ProgramStudyService,
        private studyPathService: StudyPathService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.programStudyService.query()
            .subscribe((res: ResponseWrapper) => { this.programstudies = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.studyPathService.query()
            .subscribe((res: ResponseWrapper) => { this.studypaths = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.hostDataSource.idHostDataSource !== undefined) {
            this.subscribeToSaveResponse(
                this.hostDataSourceService.update(this.hostDataSource), false);
        } else {
            this.subscribeToSaveResponse(
                this.hostDataSourceService.create(this.hostDataSource), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<HostDataSource>, isCreated: boolean) {
        result.subscribe((res: HostDataSource) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: HostDataSource, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.hostDataSource.created'
            : 'kampusApp.hostDataSource.updated',
            { param : result.idHostDataSource }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data HostDataSource di simpan !');
        this.eventManager.broadcast({ name: 'hostDataSourceListModification', content: 'OK'});
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

    trackStudyPathById(index: number, item: StudyPath) {
        return item.idStudyPath;
    }
}

@Component({
    selector: 'jhi-host-data-source-popup',
    template: ''
})
export class HostDataSourcePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hostDataSourcePopupService: HostDataSourcePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.hostDataSourcePopupService
                    .open(HostDataSourceDialogComponent, params['id']);
            } else {
                this.modalRef = this.hostDataSourcePopupService
                    .open(HostDataSourceDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
