import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudentPeriodData} from './student-period-data.model';
import {StudentPeriodDataService} from './student-period-data.service';

@Component({
    selector: 'jhi-student-period-data-detail',
    templateUrl: './student-period-data-detail.component.html'
})
export class StudentPeriodDataDetailComponent implements OnInit, OnDestroy {

    studentPeriodData: StudentPeriodData;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentPeriodDataService: StudentPeriodDataService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentPeriodData();
    }

    load(id) {
        this.studentPeriodDataService.find(id).subscribe((studentPeriodData) => {
            this.studentPeriodData = studentPeriodData;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentPeriodData() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentPeriodDataListModification',
            (response) => this.load(this.studentPeriodData.idStudentPeriod)
        );
    }
}
