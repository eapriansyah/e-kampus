import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {PostStudent} from './post-student.model';
import {PostStudentPopupService} from './post-student-popup.service';
import {PostStudentService} from './post-student.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-post-student-delete-dialog',
    templateUrl: './post-student-delete-dialog.component.html'
})
export class PostStudentDeleteDialogComponent {

    postStudent: PostStudent;

    constructor(
        private postStudentService: PostStudentService,
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
        this.postStudentService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data PostStudent di hapus !');
            this.eventManager.broadcast({
                name: 'postStudentListModification',
                content: 'Deleted an postStudent'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.postStudent.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-post-student-delete-popup',
    template: ''
})
export class PostStudentDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postStudentPopupService: PostStudentPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.postStudentPopupService
                .open(PostStudentDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
