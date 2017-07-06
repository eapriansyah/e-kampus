import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudentPeriods} from './student-periods.model';
import {StudentPeriodsService} from './student-periods.service';

@Component({
    selector: 'jhi-student-periods-detail',
    templateUrl: './student-periods-detail.component.html'
})
export class StudentPeriodsDetailComponent implements OnInit, OnDestroy {

    studentPeriods: StudentPeriods;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentPeriodsService: StudentPeriodsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentPeriods();
    }

    load(id) {
        this.studentPeriodsService.find(id).subscribe((studentPeriods) => {
            this.studentPeriods = studentPeriods;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentPeriods() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentPeriodsListModification',
            (response) => this.load(this.studentPeriods.idStudentPeriod)
        );
    }
}
