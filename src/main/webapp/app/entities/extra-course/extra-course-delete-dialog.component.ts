import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {ExtraCourse} from './extra-course.model';
import {ExtraCoursePopupService} from './extra-course-popup.service';
import {ExtraCourseService} from './extra-course.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-extra-course-delete-dialog',
    templateUrl: './extra-course-delete-dialog.component.html'
})
export class ExtraCourseDeleteDialogComponent {

    extraCourse: ExtraCourse;

    constructor(
        private extraCourseService: ExtraCourseService,
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
        this.extraCourseService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data ExtraCourse di hapus !');
            this.eventManager.broadcast({
                name: 'extraCourseListModification',
                content: 'Deleted an extraCourse'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.extraCourse.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-extra-course-delete-popup',
    template: ''
})
export class ExtraCourseDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private extraCoursePopupService: ExtraCoursePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.extraCoursePopupService
                .open(ExtraCourseDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
