import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ExtraCourse} from './extra-course.model';
import {ExtraCourseService} from './extra-course.service';

@Component({
    selector: 'jhi-extra-course-detail',
    templateUrl: './extra-course-detail.component.html'
})
export class ExtraCourseDetailComponent implements OnInit, OnDestroy {

    extraCourse: ExtraCourse;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private extraCourseService: ExtraCourseService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInExtraCourses();
    }

    load(id) {
        this.extraCourseService.find(id).subscribe((extraCourse) => {
            this.extraCourse = extraCourse;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInExtraCourses() {
        this.eventSubscriber = this.eventManager.subscribe(
            'extraCourseListModification',
            (response) => this.load(this.extraCourse.idCourse)
        );
    }
}
