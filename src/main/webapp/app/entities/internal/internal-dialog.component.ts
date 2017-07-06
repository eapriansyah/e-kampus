import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Internal} from './internal.model';
import {InternalPopupService} from './internal-popup.service';
import {InternalService} from './internal.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-internal-dialog',
    templateUrl: './internal-dialog.component.html'
})
export class InternalDialogComponent implements OnInit {

    internal: Internal;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private internalService: InternalService,
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
        if (this.internal.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.internalService.update(this.internal), false);
        } else {
            this.subscribeToSaveResponse(
                this.internalService.create(this.internal), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Internal>, isCreated: boolean) {
        result.subscribe((res: Internal) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Internal, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.internal.created'
            : 'kampusApp.internal.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Internal di simpan !');
        this.eventManager.broadcast({ name: 'internalListModification', content: 'OK'});
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
    selector: 'jhi-internal-popup',
    template: ''
})
export class InternalPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internalPopupService: InternalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.internalPopupService
                    .open(InternalDialogComponent, params['id']);
            } else {
                this.modalRef = this.internalPopupService
                    .open(InternalDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
