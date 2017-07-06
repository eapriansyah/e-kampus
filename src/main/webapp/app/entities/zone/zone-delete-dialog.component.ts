import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Zone} from './zone.model';
import {ZonePopupService} from './zone-popup.service';
import {ZoneService} from './zone.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-zone-delete-dialog',
    templateUrl: './zone-delete-dialog.component.html'
})
export class ZoneDeleteDialogComponent {

    zone: Zone;

    constructor(
        private zoneService: ZoneService,
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
        this.zoneService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Zone di hapus !');
            this.eventManager.broadcast({
                name: 'zoneListModification',
                content: 'Deleted an zone'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.zone.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-zone-delete-popup',
    template: ''
})
export class ZoneDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private zonePopupService: ZonePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.zonePopupService
                .open(ZoneDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
