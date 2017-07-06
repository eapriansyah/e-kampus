import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {AcademicPeriods} from './academic-periods.model';
import {AcademicPeriodsPopupService} from './academic-periods-popup.service';
import {AcademicPeriodsService} from './academic-periods.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-academic-periods-delete-dialog',
    templateUrl: './academic-periods-delete-dialog.component.html'
})
export class AcademicPeriodsDeleteDialogComponent {

    academicPeriods: AcademicPeriods;

    constructor(
        private academicPeriodsService: AcademicPeriodsService,
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
        this.academicPeriodsService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data AcademicPeriods di hapus !');
            this.eventManager.broadcast({
                name: 'academicPeriodsListModification',
                content: 'Deleted an academicPeriods'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.academicPeriods.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-academic-periods-delete-popup',
    template: ''
})
export class AcademicPeriodsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private academicPeriodsPopupService: AcademicPeriodsPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.academicPeriodsPopupService
                .open(AcademicPeriodsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
