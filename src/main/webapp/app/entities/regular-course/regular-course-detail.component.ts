import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {RegularCourse} from './regular-course.model';
import {RegularCourseService} from './regular-course.service';

@Component({
    selector: 'jhi-regular-course-detail',
    templateUrl: './regular-course-detail.component.html'
})
export class RegularCourseDetailComponent implements OnInit, OnDestroy {

    regularCourse: RegularCourse;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private regularCourseService: RegularCourseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInRegularCourses();
    }

    load(id) {
        this.regularCourseService.find(id).subscribe((regularCourse) => {
            this.regularCourse = regularCourse;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInRegularCourses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'regularCourseListModification',
            (response) => this.load(this.regularCourse.idCourse)
        );
    }
}
