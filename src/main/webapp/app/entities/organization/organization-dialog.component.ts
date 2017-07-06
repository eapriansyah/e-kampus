import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Organization} from './organization.model';
import {OrganizationPopupService} from './organization-popup.service';
import {OrganizationService} from './organization.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-organization-dialog',
    templateUrl: './organization-dialog.component.html'
})
export class OrganizationDialogComponent implements OnInit {

    organization: Organization;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private organizationService: OrganizationService,
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
        if (this.organization.idParty !== undefined) {
            this.subscribeToSaveResponse(
                this.organizationService.update(this.organization), false);
        } else {
            this.subscribeToSaveResponse(
                this.organizationService.create(this.organization), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Organization>, isCreated: boolean) {
        result.subscribe((res: Organization) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Organization, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.organization.created'
            : 'kampusApp.organization.updated',
            { param : result.idParty }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Organization di simpan !');
        this.eventManager.broadcast({ name: 'organizationListModification', content: 'OK'});
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
    selector: 'jhi-organization-popup',
    template: ''
})
export class OrganizationPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private organizationPopupService: OrganizationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.organizationPopupService
                    .open(OrganizationDialogComponent, params['id']);
            } else {
                this.modalRef = this.organizationPopupService
                    .open(OrganizationDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
