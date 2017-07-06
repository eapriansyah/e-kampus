import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Course} from './course.model';
import {CoursePopupService} from './course-popup.service';
import {CourseService} from './course.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-course-delete-dialog',
    templateUrl: './course-delete-dialog.component.html'
})
export class CourseDeleteDialogComponent {

    course: Course;

    constructor(
        private courseService: CourseService,
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
        this.courseService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Course di hapus !');
            this.eventManager.broadcast({
                name: 'courseListModification',
                content: 'Deleted an course'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.course.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-course-delete-popup',
    template: ''
})
export class CourseDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private coursePopupService: CoursePopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.coursePopupService
                .open(CourseDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
