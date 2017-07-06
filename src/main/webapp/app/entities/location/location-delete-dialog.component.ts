import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Location} from './location.model';
import {LocationPopupService} from './location-popup.service';
import {LocationService} from './location.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-location-delete-dialog',
    templateUrl: './location-delete-dialog.component.html'
})
export class LocationDeleteDialogComponent {

    location: Location;

    constructor(
        private locationService: LocationService,
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
        this.locationService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Location di hapus !');
            this.eventManager.broadcast({
                name: 'locationListModification',
                content: 'Deleted an location'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.location.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-location-delete-popup',
    template: ''
})
export class LocationDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private locationPopupService: LocationPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.locationPopupService
                .open(LocationDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
