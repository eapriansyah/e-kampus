import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PersonalData} from './personal-data.model';
import {PersonalDataPopupService} from './personal-data-popup.service';
import {PersonalDataService} from './personal-data.service';
import {ToasterService} from '../../shared';
import { Person, PersonService } from '../person';
import { ReligionType, ReligionTypeService } from '../religion-type';
import { EducationType, EducationTypeService } from '../education-type';
import { WorkType, WorkTypeService } from '../work-type';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-personal-data-dialog',
    templateUrl: './personal-data-dialog.component.html'
})
export class PersonalDataDialogComponent implements OnInit {

    personalData: PersonalData;
    authorities: any[];
    isSaving: boolean;

    people: Person[];

    religiontypes: ReligionType[];

    educationtypes: EducationType[];

    worktypes: WorkType[];
    fatherDobDp: any;
    motherDobDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private personalDataService: PersonalDataService,
        private personService: PersonService,
        private religionTypeService: ReligionTypeService,
        private educationTypeService: EducationTypeService,
        private workTypeService: WorkTypeService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.personService.query()
            .subscribe((res: ResponseWrapper) => { this.people = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.religionTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.religiontypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.educationTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.educationtypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.workTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.worktypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.personalData.idPersonalData !== undefined) {
            this.subscribeToSaveResponse(
                this.personalDataService.update(this.personalData), false);
        } else {
            this.subscribeToSaveResponse(
                this.personalDataService.create(this.personalData), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PersonalData>, isCreated: boolean) {
        result.subscribe((res: PersonalData) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PersonalData, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.personalData.created'
            : 'kampusApp.personalData.updated',
            { param : result.idPersonalData }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PersonalData di simpan !');
        this.eventManager.broadcast({ name: 'personalDataListModification', content: 'OK'});
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

    trackPersonById(index: number, item: Person) {
        return item.idParty;
    }

    trackReligionTypeById(index: number, item: ReligionType) {
        return item.idReligionType;
    }

    trackEducationTypeById(index: number, item: EducationType) {
        return item.idEducationType;
    }

    trackWorkTypeById(index: number, item: WorkType) {
        return item.idWorkType;
    }
}

@Component({
    selector: 'jhi-personal-data-popup',
    template: ''
})
export class PersonalDataPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private personalDataPopupService: PersonalDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.personalDataPopupService
                    .open(PersonalDataDialogComponent, params['id']);
            } else {
                this.modalRef = this.personalDataPopupService
                    .open(PersonalDataDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
