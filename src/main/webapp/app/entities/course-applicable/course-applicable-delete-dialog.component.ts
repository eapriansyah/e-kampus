import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {CourseApplicable} from './course-applicable.model';
import {CourseApplicablePopupService} from './course-applicable-popup.service';
import {CourseApplicableService} from './course-applicable.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-course-applicable-delete-dialog',
    templateUrl: './course-applicable-delete-dialog.component.html'
})
export class CourseApplicableDeleteDialogComponent {

    courseApplicable: CourseApplicable;

    constructor(
        private courseApplicableService: CourseApplicableService,
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
        this.courseApplicableService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data CourseApplicable di hapus !');
            this.eventManager.broadcast({
                name: 'courseApplicableListModification',
                content: 'Deleted an courseApplicable'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.courseApplicable.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-course-applicable-delete-popup',
    template: ''
})
export class CourseApplicableDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courseApplicablePopupService: CourseApplicablePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.courseApplicablePopupService
                .open(CourseApplicableDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
