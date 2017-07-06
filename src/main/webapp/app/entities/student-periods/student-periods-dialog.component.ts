import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {StudentPeriods} from './student-periods.model';
import {StudentPeriodsPopupService} from './student-periods-popup.service';
import {StudentPeriodsService} from './student-periods.service';
import {ToasterService} from '../../shared';
import { Student, StudentService } from '../student';
import { AcademicPeriods, AcademicPeriodsService } from '../academic-periods';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-student-periods-dialog',
    templateUrl: './student-periods-dialog.component.html'
})
export class StudentPeriodsDialogComponent implements OnInit {

    studentPeriods: StudentPeriods;
    authorities: any[];
    isSaving: boolean;

    students: Student[];

    academicperiods: AcademicPeriods[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private studentPeriodsService: StudentPeriodsService,
        private studentService: StudentService,
        private academicPeriodsService: AcademicPeriodsService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.studentService.query()
            .subscribe((res: ResponseWrapper) => { this.students = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.academicPeriodsService.query()
            .subscribe((res: ResponseWrapper) => { this.academicperiods = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.studentPeriods.idStudentPeriod !== undefined) {
            this.subscribeToSaveResponse(
                this.studentPeriodsService.update(this.studentPeriods), false);
        } else {
            this.subscribeToSaveResponse(
                this.studentPeriodsService.create(this.studentPeriods), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<StudentPeriods>, isCreated: boolean) {
        result.subscribe((res: StudentPeriods) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: StudentPeriods, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.studentPeriods.created'
            : 'kampusApp.studentPeriods.updated',
            { param : result.idStudentPeriod }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data StudentPeriods di simpan !');
        this.eventManager.broadcast({ name: 'studentPeriodsListModification', content: 'OK'});
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

    trackStudentById(index: number, item: Student) {
        return item.idPartyRole;
    }

    trackAcademicPeriodsById(index: number, item: AcademicPeriods) {
        return item.idPeriod;
    }
}

@Component({
    selector: 'jhi-student-periods-popup',
    template: ''
})
export class StudentPeriodsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPeriodsPopupService: StudentPeriodsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.studentPeriodsPopupService
                    .open(StudentPeriodsDialogComponent, params['id']);
            } else {
                this.modalRef = this.studentPeriodsPopupService
                    .open(StudentPeriodsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
