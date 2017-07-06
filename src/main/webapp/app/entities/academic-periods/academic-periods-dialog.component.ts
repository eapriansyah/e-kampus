import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {AcademicPeriods} from './academic-periods.model';
import {AcademicPeriodsPopupService} from './academic-periods-popup.service';
import {AcademicPeriodsService} from './academic-periods.service';
import {ToasterService} from '../../shared';
import { PeriodType, PeriodTypeService } from '../period-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-academic-periods-dialog',
    templateUrl: './academic-periods-dialog.component.html'
})
export class AcademicPeriodsDialogComponent implements OnInit {

    academicPeriods: AcademicPeriods;
    authorities: any[];
    isSaving: boolean;

    periodtypes: PeriodType[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private academicPeriodsService: AcademicPeriodsService,
        private periodTypeService: PeriodTypeService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.periodTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.periodtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.academicPeriods.idPeriod !== undefined) {
            this.subscribeToSaveResponse(
                this.academicPeriodsService.update(this.academicPeriods), false);
        } else {
            this.subscribeToSaveResponse(
                this.academicPeriodsService.create(this.academicPeriods), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<AcademicPeriods>, isCreated: boolean) {
        result.subscribe((res: AcademicPeriods) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AcademicPeriods, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.academicPeriods.created'
            : 'kampusApp.academicPeriods.updated',
            { param : result.idPeriod }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data AcademicPeriods di simpan !');
        this.eventManager.broadcast({ name: 'academicPeriodsListModification', content: 'OK'});
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

    trackPeriodTypeById(index: number, item: PeriodType) {
        return item.idPeriodType;
    }
}

@Component({
    selector: 'jhi-academic-periods-popup',
    template: ''
})
export class AcademicPeriodsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private academicPeriodsPopupService: AcademicPeriodsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.academicPeriodsPopupService
                    .open(AcademicPeriodsDialogComponent, params['id']);
            } else {
                this.modalRef = this.academicPeriodsPopupService
                    .open(AcademicPeriodsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
