import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {ContactMechanism} from './contact-mechanism.model';
import {ContactMechanismPopupService} from './contact-mechanism-popup.service';
import {ContactMechanismService} from './contact-mechanism.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-contact-mechanism-dialog',
    templateUrl: './contact-mechanism-dialog.component.html'
})
export class ContactMechanismDialogComponent implements OnInit {

    contactMechanism: ContactMechanism;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private contactMechanismService: ContactMechanismService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.contactMechanism.idContact !== undefined) {
            this.subscribeToSaveResponse(
                this.contactMechanismService.update(this.contactMechanism), false);
        } else {
            this.subscribeToSaveResponse(
                this.contactMechanismService.create(this.contactMechanism), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<ContactMechanism>, isCreated: boolean) {
        result.subscribe((res: ContactMechanism) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: ContactMechanism, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.contactMechanism.created'
            : 'kampusApp.contactMechanism.updated',
            { param : result.idContact }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data ContactMechanism di simpan !');
        this.eventManager.broadcast({ name: 'contactMechanismListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-contact-mechanism-popup',
    template: ''
})
export class ContactMechanismPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactMechanismPopupService: ContactMechanismPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.contactMechanismPopupService
                    .open(ContactMechanismDialogComponent, params['id']);
            } else {
                this.modalRef = this.contactMechanismPopupService
                    .open(ContactMechanismDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
