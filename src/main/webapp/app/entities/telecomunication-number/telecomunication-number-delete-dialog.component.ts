import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {TelecomunicationNumber} from './telecomunication-number.model';
import {TelecomunicationNumberPopupService} from './telecomunication-number-popup.service';
import {TelecomunicationNumberService} from './telecomunication-number.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-telecomunication-number-delete-dialog',
    templateUrl: './telecomunication-number-delete-dialog.component.html'
})
export class TelecomunicationNumberDeleteDialogComponent {

    telecomunicationNumber: TelecomunicationNumber;

    constructor(
        private telecomunicationNumberService: TelecomunicationNumberService,
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
        this.telecomunicationNumberService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data TelecomunicationNumber di hapus !');
            this.eventManager.broadcast({
                name: 'telecomunicationNumberListModification',
                content: 'Deleted an telecomunicationNumber'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.telecomunicationNumber.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-telecomunication-number-delete-popup',
    template: ''
})
export class TelecomunicationNumberDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private telecomunicationNumberPopupService: TelecomunicationNumberPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.telecomunicationNumberPopupService
                .open(TelecomunicationNumberDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
