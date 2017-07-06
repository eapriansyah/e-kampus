import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {WorkType} from './work-type.model';
import {WorkTypePopupService} from './work-type-popup.service';
import {WorkTypeService} from './work-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-work-type-delete-dialog',
    templateUrl: './work-type-delete-dialog.component.html'
})
export class WorkTypeDeleteDialogComponent {

    workType: WorkType;

    constructor(
        private workTypeService: WorkTypeService,
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
        this.workTypeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data WorkType di hapus !');
            this.eventManager.broadcast({
                name: 'workTypeListModification',
                content: 'Deleted an workType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.workType.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-work-type-delete-popup',
    template: ''
})
export class WorkTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private workTypePopupService: WorkTypePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.workTypePopupService
                .open(WorkTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
