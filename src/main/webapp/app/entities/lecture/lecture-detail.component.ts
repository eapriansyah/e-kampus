import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Lecture} from './lecture.model';
import {LectureService} from './lecture.service';

@Component({
    selector: 'jhi-lecture-detail',
    templateUrl: './lecture-detail.component.html'
})
export class LectureDetailComponent implements OnInit, OnDestroy {

    lecture: Lecture;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private lectureService: LectureService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLectures();
    }

    load(id) {
        this.lectureService.find(id).subscribe((lecture) => {
            this.lecture = lecture;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLectures() {
        this.eventSubscriber = this.eventManager.subscribe(
            'lectureListModification',
            (response) => this.load(this.lecture.idPartyRole)
        );
    }
}
