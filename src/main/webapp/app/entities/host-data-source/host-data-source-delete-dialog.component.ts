import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {HostDataSource} from './host-data-source.model';
import {HostDataSourcePopupService} from './host-data-source-popup.service';
import {HostDataSourceService} from './host-data-source.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-host-data-source-delete-dialog',
    templateUrl: './host-data-source-delete-dialog.component.html'
})
export class HostDataSourceDeleteDialogComponent {

    hostDataSource: HostDataSource;

    constructor(
        private hostDataSourceService: HostDataSourceService,
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
        this.hostDataSourceService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data HostDataSource di hapus !');
            this.eventManager.broadcast({
                name: 'hostDataSourceListModification',
                content: 'Deleted an hostDataSource'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.hostDataSource.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-host-data-source-delete-popup',
    template: ''
})
export class HostDataSourceDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hostDataSourcePopupService: HostDataSourcePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.hostDataSourcePopupService
                .open(HostDataSourceDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
