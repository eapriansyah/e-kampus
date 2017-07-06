import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ContactMechanism} from './contact-mechanism.model';
import {ContactMechanismPopupService} from './contact-mechanism-popup.service';
import {ContactMechanismService} from './contact-mechanism.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-contact-mechanism-delete-dialog',
    templateUrl: './contact-mechanism-delete-dialog.component.html'
})
export class ContactMechanismDeleteDialogComponent {

    contactMechanism: ContactMechanism;

    constructor(
        private contactMechanismService: ContactMechanismService,
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
        this.contactMechanismService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ContactMechanism di hapus !');
            this.eventManager.broadcast({
                name: 'contactMechanismListModification',
                content: 'Deleted an contactMechanism'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.contactMechanism.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-contact-mechanism-delete-popup',
    template: ''
})
export class ContactMechanismDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactMechanismPopupService: ContactMechanismPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.contactMechanismPopupService
                .open(ContactMechanismDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
