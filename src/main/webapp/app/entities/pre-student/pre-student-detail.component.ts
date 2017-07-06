import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {PreStudent} from './pre-student.model';
import {PreStudentService} from './pre-student.service';

@Component({
    selector: 'jhi-pre-student-detail',
    templateUrl: './pre-student-detail.component.html'
})
export class PreStudentDetailComponent implements OnInit, OnDestroy {

    preStudent: PreStudent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private preStudentService: PreStudentService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPreStudents();
    }

    load(id) {
        this.preStudentService.find(id).subscribe((preStudent) => {
            this.preStudent = preStudent;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPreStudents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'preStudentListModification',
            (response) => this.load(this.preStudent.idPartyRole)
        );
    }
}
