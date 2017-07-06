import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ContactMechanismPurpose} from './contact-mechanism-purpose.model';
import {ContactMechanismPurposePopupService} from './contact-mechanism-purpose-popup.service';
import {ContactMechanismPurposeService} from './contact-mechanism-purpose.service';
import {ToasterService} from '../../shared';
import { PurposeType, PurposeTypeService } from '../purpose-type';
import { Party, PartyService } from '../party';
import { ContactMechanism, ContactMechanismService } from '../contact-mechanism';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-contact-mechanism-purpose-dialog',
    templateUrl: './contact-mechanism-purpose-dialog.component.html'
})
export class ContactMechanismPurposeDialogComponent implements OnInit {

    contactMechanismPurpose: ContactMechanismPurpose;
    authorities: any[];
    isSaving: boolean;

    purposetypes: PurposeType[];

    parties: Party[];

    contactmechanisms: ContactMechanism[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private contactMechanismPurposeService: ContactMechanismPurposeService,
        private purposeTypeService: PurposeTypeService,
        private partyService: PartyService,
        private contactMechanismService: ContactMechanismService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.purposeTypeService.query()
            .subscribe((res: ResponseWrapper) => { this.purposetypes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.partyService.query()
            .subscribe((res: ResponseWrapper) => { this.parties = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.contactMechanismService.query()
            .subscribe((res: ResponseWrapper) => { this.contactmechanisms = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contactMechanismPurpose.idContactMechPurpose !== undefined) {
            this.subscribeToSaveResponse(
                this.contactMechanismPurposeService.update(this.contactMechanismPurpose), false);
        } else {
            this.subscribeToSaveResponse(
                this.contactMechanismPurposeService.create(this.contactMechanismPurpose), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ContactMechanismPurpose>, isCreated: boolean) {
        result.subscribe((res: ContactMechanismPurpose) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ContactMechanismPurpose, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.contactMechanismPurpose.created'
            : 'kampusApp.contactMechanismPurpose.updated',
            { param : result.idContactMechPurpose }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ContactMechanismPurpose di simpan !');
        this.eventManager.broadcast({ name: 'contactMechanismPurposeListModification', content: 'OK'});
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

    trackPurposeTypeById(index: number, item: PurposeType) {
        return item.idPurposeType;
    }

    trackPartyById(index: number, item: Party) {
        return item.idParty;
    }

    trackContactMechanismById(index: number, item: ContactMechanism) {
        return item.idContact;
    }
}

@Component({
    selector: 'jhi-contact-mechanism-purpose-popup',
    template: ''
})
export class ContactMechanismPurposePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactMechanismPurposePopupService: ContactMechanismPurposePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.contactMechanismPurposePopupService
                    .open(ContactMechanismPurposeDialogComponent, params['id']);
            } else {
                this.modalRef = this.contactMechanismPurposePopupService
                    .open(ContactMechanismPurposeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
