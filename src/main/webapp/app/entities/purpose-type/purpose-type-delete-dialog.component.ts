import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PurposeType} from './purpose-type.model';
import {PurposeTypePopupService} from './purpose-type-popup.service';
import {PurposeTypeService} from './purpose-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-purpose-type-delete-dialog',
    templateUrl: './purpose-type-delete-dialog.component.html'
})
export class PurposeTypeDeleteDialogComponent {

    purposeType: PurposeType;

    constructor(
        private purposeTypeService: PurposeTypeService,
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
        this.purposeTypeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PurposeType di hapus !');
            this.eventManager.broadcast({
                name: 'purposeTypeListModification',
                content: 'Deleted an purposeType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.purposeType.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-purpose-type-delete-popup',
    template: ''
})
export class PurposeTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private purposeTypePopupService: PurposeTypePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.purposeTypePopupService
                .open(PurposeTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
