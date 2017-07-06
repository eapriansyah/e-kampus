import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {University} from './university.model';
import {UniversityPopupService} from './university-popup.service';
import {UniversityService} from './university.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-university-delete-dialog',
    templateUrl: './university-delete-dialog.component.html'
})
export class UniversityDeleteDialogComponent {

    university: University;

    constructor(
        private universityService: UniversityService,
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
        this.universityService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data University di hapus !');
            this.eventManager.broadcast({
                name: 'universityListModification',
                content: 'Deleted an university'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.university.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-university-delete-popup',
    template: ''
})
export class UniversityDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private universityPopupService: UniversityPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.universityPopupService
                .open(UniversityDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
