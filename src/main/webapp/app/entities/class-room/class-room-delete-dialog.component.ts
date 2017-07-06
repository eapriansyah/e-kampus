import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ClassRoom} from './class-room.model';
import {ClassRoomPopupService} from './class-room-popup.service';
import {ClassRoomService} from './class-room.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-class-room-delete-dialog',
    templateUrl: './class-room-delete-dialog.component.html'
})
export class ClassRoomDeleteDialogComponent {

    classRoom: ClassRoom;

    constructor(
        private classRoomService: ClassRoomService,
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
        this.classRoomService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ClassRoom di hapus !');
            this.eventManager.broadcast({
                name: 'classRoomListModification',
                content: 'Deleted an classRoom'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.classRoom.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-class-room-delete-popup',
    template: ''
})
export class ClassRoomDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private classRoomPopupService: ClassRoomPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.classRoomPopupService
                .open(ClassRoomDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
