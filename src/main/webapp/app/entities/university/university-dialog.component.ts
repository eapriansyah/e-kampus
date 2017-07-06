import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {University} from './university.model';
import {UniversityPopupService} from './university-popup.service';
import {UniversityService} from './university.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-university-dialog',
    templateUrl: './university-dialog.component.html'
})
export class UniversityDialogComponent implements OnInit {

    university: University;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private universityService: UniversityService,
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
        if (this.university.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.universityService.update(this.university), false);
        } else {
            this.subscribeToSaveResponse(
                this.universityService.create(this.university), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<University>, isCreated: boolean) {
        result.subscribe((res: University) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: University, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.university.created'
            : 'kampusApp.university.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data University di simpan !');
        this.eventManager.broadcast({ name: 'universityListModification', content: 'OK'});
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
    selector: 'jhi-university-popup',
    template: ''
})
export class UniversityPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private universityPopupService: UniversityPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.universityPopupService
                    .open(UniversityDialogComponent, params['id']);
            } else {
                this.modalRef = this.universityPopupService
                    .open(UniversityDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
