import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ReligionType} from './religion-type.model';
import {ReligionTypePopupService} from './religion-type-popup.service';
import {ReligionTypeService} from './religion-type.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-religion-type-delete-dialog',
    templateUrl: './religion-type-delete-dialog.component.html'
})
export class ReligionTypeDeleteDialogComponent {

    religionType: ReligionType;

    constructor(
        private religionTypeService: ReligionTypeService,
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
        this.religionTypeService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ReligionType di hapus !');
            this.eventManager.broadcast({
                name: 'religionTypeListModification',
                content: 'Deleted an religionType'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.religionType.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-religion-type-delete-popup',
    template: ''
})
export class ReligionTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private religionTypePopupService: ReligionTypePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.religionTypePopupService
                .open(ReligionTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
