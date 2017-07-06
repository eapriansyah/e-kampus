import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {RegularCourse} from './regular-course.model';
import {RegularCoursePopupService} from './regular-course-popup.service';
import {RegularCourseService} from './regular-course.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-regular-course-delete-dialog',
    templateUrl: './regular-course-delete-dialog.component.html'
})
export class RegularCourseDeleteDialogComponent {

    regularCourse: RegularCourse;

    constructor(
        private regularCourseService: RegularCourseService,
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
        this.regularCourseService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data RegularCourse di hapus !');
            this.eventManager.broadcast({
                name: 'regularCourseListModification',
                content: 'Deleted an regularCourse'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.regularCourse.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-regular-course-delete-popup',
    template: ''
})
export class RegularCourseDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private regularCoursePopupService: RegularCoursePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.regularCoursePopupService
                .open(RegularCourseDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
