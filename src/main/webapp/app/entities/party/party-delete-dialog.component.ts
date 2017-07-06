import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Party} from './party.model';
import {PartyPopupService} from './party-popup.service';
import {PartyService} from './party.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-party-delete-dialog',
    templateUrl: './party-delete-dialog.component.html'
})
export class PartyDeleteDialogComponent {

    party: Party;

    constructor(
        private partyService: PartyService,
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
        this.partyService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Party di hapus !');
            this.eventManager.broadcast({
                name: 'partyListModification',
                content: 'Deleted an party'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.party.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-party-delete-popup',
    template: ''
})
export class PartyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private partyPopupService: PartyPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.partyPopupService
                .open(PartyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
