import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ClassStudy} from './class-study.model';
import {ClassStudyPopupService} from './class-study-popup.service';
import {ClassStudyService} from './class-study.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-class-study-delete-dialog',
    templateUrl: './class-study-delete-dialog.component.html'
})
export class ClassStudyDeleteDialogComponent {

    classStudy: ClassStudy;

    constructor(
        private classStudyService: ClassStudyService,
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
        this.classStudyService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ClassStudy di hapus !');
            this.eventManager.broadcast({
                name: 'classStudyListModification',
                content: 'Deleted an classStudy'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.classStudy.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-class-study-delete-popup',
    template: ''
})
export class ClassStudyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private classStudyPopupService: ClassStudyPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.classStudyPopupService
                .open(ClassStudyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
