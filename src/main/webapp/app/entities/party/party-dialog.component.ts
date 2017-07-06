import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Party} from './party.model';
import {PartyPopupService} from './party-popup.service';
import {PartyService} from './party.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-party-dialog',
    templateUrl: './party-dialog.component.html'
})
export class PartyDialogComponent implements OnInit {

    party: Party;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private partyService: PartyService,
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
        if (this.party.idParty !== undefined) {
            this.subscribeToSaveResponse(
                this.partyService.update(this.party), false);
        } else {
            this.subscribeToSaveResponse(
                this.partyService.create(this.party), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Party>, isCreated: boolean) {
        result.subscribe((res: Party) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Party, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.party.created'
            : 'kampusApp.party.updated',
            { param : result.idParty }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Party di simpan !');
        this.eventManager.broadcast({ name: 'partyListModification', content: 'OK'});
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
    selector: 'jhi-party-popup',
    template: ''
})
export class PartyPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private partyPopupService: PartyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.partyPopupService
                    .open(PartyDialogComponent, params['id']);
            } else {
                this.modalRef = this.partyPopupService
                    .open(PartyDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
