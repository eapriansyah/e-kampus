import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudentCoursePeriod} from './student-course-period.model';
import {StudentCoursePeriodService} from './student-course-period.service';

@Component({
    selector: 'jhi-student-course-period-detail',
    templateUrl: './student-course-period-detail.component.html'
})
export class StudentCoursePeriodDetailComponent implements OnInit, OnDestroy {

    studentCoursePeriod: StudentCoursePeriod;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentCoursePeriodService: StudentCoursePeriodService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentCoursePeriods();
    }

    load(id) {
        this.studentCoursePeriodService.find(id).subscribe((studentCoursePeriod) => {
            this.studentCoursePeriod = studentCoursePeriod;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentCoursePeriods() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentCoursePeriodListModification',
            (response) => this.load(this.studentCoursePeriod.idStudentCoursePeriod)
        );
    }
}
