import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Response} from '@angular/http';

import {Observable} from 'rxjs/Rx';
import {NgbActiveModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiEventManager, JhiAlertService} from 'ng-jhipster';

import {PostStudent} from './post-student.model';
import {PostStudentPopupService} from './post-student-popup.service';
import {PostStudentService} from './post-student.service';
import {ToasterService} from '../../shared';

@Component({
    selector: 'jhi-post-student-dialog',
    templateUrl: './post-student-dialog.component.html'
})
export class PostStudentDialogComponent implements OnInit {

    postStudent: PostStudent;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private postStudentService: PostStudentService,
        private eventManager: JhiEventManager,
        private toaster: ToasterService
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }


    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.postStudent.idPartyRole !== undefined) {
            this.subscribeToSaveResponse(
                this.postStudentService.update(this.postStudent), false);
        } else {
            this.subscribeToSaveResponse(
                this.postStudentService.create(this.postStudent), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<PostStudent>, isCreated: boolean) {
        result.subscribe((res: PostStudent) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PostStudent, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'kampusApp.postStudent.created'
            : 'kampusApp.postStudent.updated',
            { param : result.idPartyRole }, null);

        this.toaster.showToaster('info', 'Data Save', 'Data PostStudent di simpan !');
        this.eventManager.broadcast({ name: 'postStudentListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}

@Component({
    selector: 'jhi-post-student-popup',
    template: ''
})
export class PostStudentPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private postStudentPopupService: PostStudentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.postStudentPopupService
                    .open(PostStudentDialogComponent, params['id']);
            } else {
                this.modalRef = this.postStudentPopupService
                    .open(PostStudentDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
