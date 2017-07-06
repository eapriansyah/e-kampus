import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PeriodType} from './period-type.model';
import {PeriodTypePopupService} from './period-type-popup.service';
import {PeriodTypeService} from './period-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-period-type-delete-dialog',
    templateUrl: './period-type-delete-dialog.component.html'
})
export class PeriodTypeDeleteDialogComponent {

    periodType: PeriodType;

    constructor(
        private periodTypeService: PeriodTypeService,
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
        this.periodTypeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PeriodType di hapus !');
            this.eventManager.broadcast({
                name: 'periodTypeListModification',
                content: 'Deleted an periodType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.periodType.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-period-type-delete-popup',
    template: ''
})
export class PeriodTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private periodTypePopupService: PeriodTypePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.periodTypePopupService
                .open(PeriodTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
