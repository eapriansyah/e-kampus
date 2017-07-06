import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Organization} from './organization.model';
import {OrganizationPopupService} from './organization-popup.service';
import {OrganizationService} from './organization.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-organization-delete-dialog',
    templateUrl: './organization-delete-dialog.component.html'
})
export class OrganizationDeleteDialogComponent {

    organization: Organization;

    constructor(
        private organizationService: OrganizationService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id?: any) {
        this.organizationService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Organization di hapus !');
            this.eventManager.broadcast({
                name: 'organizationListModification',
                content: 'Deleted an organization'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.organization.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-organization-delete-popup',
    template: ''
})
export class OrganizationDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private organizationPopupService: OrganizationPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.organizationPopupService
                .open(OrganizationDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
