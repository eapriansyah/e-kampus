import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService,JhiEventManager } from 'ng-jhipster';

import {Student} from './student.model';
import {StudentPopupService} from './student-popup.service';
import {StudentService} from './student.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-student-delete-dialog',
    templateUrl: './student-delete-dialog.component.html'
})
export class StudentDeleteDialogComponent {

    student: Student;

    constructor(
        private studentService: StudentService,
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
        this.studentService.delete(id).subscribe((response) => {
            this.toaster.showToaster('info', 'Data Deleted', 'Data Student di hapus !');
            this.eventManager.broadcast({
                name: 'studentListModification',
                content: 'Deleted an student'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('kampusApp.student.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-student-delete-popup',
    template: ''
})
export class StudentDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private studentPopupService: StudentPopupService,
        private toaster: ToasterService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.studentPopupService
                .open(StudentDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
