import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Internal} from './internal.model';
import {InternalPopupService} from './internal-popup.service';
import {InternalService} from './internal.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-internal-delete-dialog',
    templateUrl: './internal-delete-dialog.component.html'
})
export class InternalDeleteDialogComponent {

    internal: Internal;

    constructor(
        private internalService: InternalService,
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
        this.internalService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Internal di hapus !');
            this.eventManager.broadcast({
                name: 'internalListModification',
                content: 'Deleted an internal'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.internal.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-internal-delete-popup',
    template: ''
})
export class InternalDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internalPopupService: InternalPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.internalPopupService
                .open(InternalDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
