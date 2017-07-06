import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ContactMechanismPurpose} from './contact-mechanism-purpose.model';
import {ContactMechanismPurposePopupService} from './contact-mechanism-purpose-popup.service';
import {ContactMechanismPurposeService} from './contact-mechanism-purpose.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-contact-mechanism-purpose-delete-dialog',
    templateUrl: './contact-mechanism-purpose-delete-dialog.component.html'
})
export class ContactMechanismPurposeDeleteDialogComponent {

    contactMechanismPurpose: ContactMechanismPurpose;

    constructor(
        private contactMechanismPurposeService: ContactMechanismPurposeService,
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
        this.contactMechanismPurposeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ContactMechanismPurpose di hapus !');
            this.eventManager.broadcast({
                name: 'contactMechanismPurposeListModification',
                content: 'Deleted an contactMechanismPurpose'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.contactMechanismPurpose.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-contact-mechanism-purpose-delete-popup',
    template: ''
})
export class ContactMechanismPurposeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactMechanismPurposePopupService: ContactMechanismPurposePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.contactMechanismPurposePopupService
                .open(ContactMechanismPurposeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
