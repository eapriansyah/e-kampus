import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Degree} from './degree.model';
import {DegreePopupService} from './degree-popup.service';
import {DegreeService} from './degree.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-degree-delete-dialog',
    templateUrl: './degree-delete-dialog.component.html'
})
export class DegreeDeleteDialogComponent {

    degree: Degree;

    constructor(
        private degreeService: DegreeService,
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
        this.degreeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Degree di hapus !');
            this.eventManager.broadcast({
                name: 'degreeListModification',
                content: 'Deleted an degree'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.degree.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-degree-delete-popup',
    template: ''
})
export class DegreeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private degreePopupService: DegreePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.degreePopupService
                .open(DegreeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
