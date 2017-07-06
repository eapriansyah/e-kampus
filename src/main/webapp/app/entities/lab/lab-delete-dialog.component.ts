import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Lab} from './lab.model';
import {LabPopupService} from './lab-popup.service';
import {LabService} from './lab.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-lab-delete-dialog',
    templateUrl: './lab-delete-dialog.component.html'
})
export class LabDeleteDialogComponent {

    lab: Lab;

    constructor(
        private labService: LabService,
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
        this.labService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Lab di hapus !');
            this.eventManager.broadcast({
                name: 'labListModification',
                content: 'Deleted an lab'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.lab.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-lab-delete-popup',
    template: ''
})
export class LabDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private labPopupService: LabPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.labPopupService
                .open(LabDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
