import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {StudyPath} from './study-path.model';
import {StudyPathPopupService} from './study-path-popup.service';
import {StudyPathService} from './study-path.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-study-path-delete-dialog',
    templateUrl: './study-path-delete-dialog.component.html'
})
export class StudyPathDeleteDialogComponent {

    studyPath: StudyPath;

    constructor(
        private studyPathService: StudyPathService,
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
        this.studyPathService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data StudyPath di hapus !');
            this.eventManager.broadcast({
                name: 'studyPathListModification',
                content: 'Deleted an studyPath'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.studyPath.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-study-path-delete-popup',
    template: ''
})
export class StudyPathDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studyPathPopupService: StudyPathPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studyPathPopupService
                .open(StudyPathDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
