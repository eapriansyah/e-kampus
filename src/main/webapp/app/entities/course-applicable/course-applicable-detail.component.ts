import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {CourseApplicable} from './course-applicable.model';
import {CourseApplicableService} from './course-applicable.service';

@Component({
    selector: 'jhi-course-applicable-detail',
    templateUrl: './course-applicable-detail.component.html'
})
export class CourseApplicableDetailComponent implements OnInit, OnDestroy {

    courseApplicable: CourseApplicable;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courseApplicableService: CourseApplicableService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourseApplicables();
    }

    load(id) {
        this.courseApplicableService.find(id).subscribe((courseApplicable) => {
            this.courseApplicable = courseApplicable;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourseApplicables() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courseApplicableListModification',
            (response) => this.load(this.courseApplicable.idApplCourse)
        );
    }
}
