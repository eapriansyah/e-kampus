import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ProgramStudy} from './program-study.model';
import {ProgramStudyPopupService} from './program-study-popup.service';
import {ProgramStudyService} from './program-study.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-program-study-delete-dialog',
    templateUrl: './program-study-delete-dialog.component.html'
})
export class ProgramStudyDeleteDialogComponent {

    programStudy: ProgramStudy;

    constructor(
        private programStudyService: ProgramStudyService,
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
        this.programStudyService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ProgramStudy di hapus !');
            this.eventManager.broadcast({
                name: 'programStudyListModification',
                content: 'Deleted an programStudy'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.programStudy.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-program-study-delete-popup',
    template: ''
})
export class ProgramStudyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private programStudyPopupService: ProgramStudyPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.programStudyPopupService
                .open(ProgramStudyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
