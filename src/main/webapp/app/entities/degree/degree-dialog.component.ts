import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {Degree} from './degree.model';
import {DegreePopupService} from './degree-popup.service';
import {DegreeService} from './degree.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-degree-dialog',
    templateUrl: './degree-dialog.component.html'
})
export class DegreeDialogComponent implements OnInit {

    degree: Degree;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private degreeService: DegreeService,
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
        if (this.degree.idDegree !== undefined) {
            this.subscribeToSaveResponse(
                this.degreeService.update(this.degree), false);
        } else {
            this.subscribeToSaveResponse(
                this.degreeService.create(this.degree), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<Degree>, isCreated: boolean) {
        result.subscribe((res: Degree) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Degree, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.degree.created'
            : 'kampusApp.degree.updated',
            { param : result.idDegree }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data Degree di simpan !');
        this.eventManager.broadcast({ name: 'degreeListModification', content: 'OK'});
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
    selector: 'jhi-degree-popup',
    template: ''
})
export class DegreePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private degreePopupService: DegreePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.degreePopupService
                    .open(DegreeDialogComponent, params['id']);
            } else {
                this.modalRef = this.degreePopupService
                    .open(DegreeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
