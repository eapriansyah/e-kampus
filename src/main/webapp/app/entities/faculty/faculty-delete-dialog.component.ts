import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Faculty} from './faculty.model';
import {FacultyPopupService} from './faculty-popup.service';
import {FacultyService} from './faculty.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-faculty-delete-dialog',
    templateUrl: './faculty-delete-dialog.component.html'
})
export class FacultyDeleteDialogComponent {

    faculty: Faculty;

    constructor(
        private facultyService: FacultyService,
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
        this.facultyService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Faculty di hapus !');
            this.eventManager.broadcast({
                name: 'facultyListModification',
                content: 'Deleted an faculty'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.faculty.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-faculty-delete-popup',
    template: ''
})
export class FacultyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private facultyPopupService: FacultyPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.facultyPopupService
                .open(FacultyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
