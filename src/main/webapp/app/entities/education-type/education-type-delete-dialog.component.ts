import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {EducationType} from './education-type.model';
import {EducationTypePopupService} from './education-type-popup.service';
import {EducationTypeService} from './education-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-education-type-delete-dialog',
    templateUrl: './education-type-delete-dialog.component.html'
})
export class EducationTypeDeleteDialogComponent {

    educationType: EducationType;

    constructor(
        private educationTypeService: EducationTypeService,
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
        this.educationTypeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data EducationType di hapus !');
            this.eventManager.broadcast({
                name: 'educationTypeListModification',
                content: 'Deleted an educationType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.educationType.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-education-type-delete-popup',
    template: ''
})
export class EducationTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private educationTypePopupService: EducationTypePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.educationTypePopupService
                .open(EducationTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
