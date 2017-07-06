import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Building} from './building.model';
import {BuildingPopupService} from './building-popup.service';
import {BuildingService} from './building.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-building-delete-dialog',
    templateUrl: './building-delete-dialog.component.html'
})
export class BuildingDeleteDialogComponent {

    building: Building;

    constructor(
        private buildingService: BuildingService,
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
        this.buildingService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Building di hapus !');
            this.eventManager.broadcast({
                name: 'buildingListModification',
                content: 'Deleted an building'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.building.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-building-delete-popup',
    template: ''
})
export class BuildingDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private buildingPopupService: BuildingPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.buildingPopupService
                .open(BuildingDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
