import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {CourseLecture} from './course-lecture.model';
import {CourseLectureService} from './course-lecture.service';

@Component({
    selector: 'jhi-course-lecture-detail',
    templateUrl: './course-lecture-detail.component.html'
})
export class CourseLectureDetailComponent implements OnInit, OnDestroy {

    courseLecture: CourseLecture;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courseLectureService: CourseLectureService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourseLectures();
    }

    load(id) {
        this.courseLectureService.find(id).subscribe((courseLecture) => {
            this.courseLecture = courseLecture;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourseLectures() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courseLectureListModification',
            (response) => this.load(this.courseLecture.idCourseLecture)
        );
    }
}
