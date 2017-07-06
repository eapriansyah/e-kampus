import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PersonalData} from './personal-data.model';
import {PersonalDataPopupService} from './personal-data-popup.service';
import {PersonalDataService} from './personal-data.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-personal-data-delete-dialog',
    templateUrl: './personal-data-delete-dialog.component.html'
})
export class PersonalDataDeleteDialogComponent {

    personalData: PersonalData;

    constructor(
        private personalDataService: PersonalDataService,
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
        this.personalDataService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PersonalData di hapus !');
            this.eventManager.broadcast({
                name: 'personalDataListModification',
                content: 'Deleted an personalData'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.personalData.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-personal-data-delete-popup',
    template: ''
})
export class PersonalDataDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private personalDataPopupService: PersonalDataPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.personalDataPopupService
                .open(PersonalDataDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
