import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudentCourseScore} from './student-course-score.model';
import {StudentCourseScoreService} from './student-course-score.service';

@Component({
    selector: 'jhi-student-course-score-detail',
    templateUrl: './student-course-score-detail.component.html'
})
export class StudentCourseScoreDetailComponent implements OnInit, OnDestroy {

    studentCourseScore: StudentCourseScore;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentCourseScoreService: StudentCourseScoreService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentCourseScores();
    }

    load(id) {
        this.studentCourseScoreService.find(id).subscribe((studentCourseScore) => {
            this.studentCourseScore = studentCourseScore;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentCourseScores() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentCourseScoreListModification',
            (response) => this.load(this.studentCourseScore.idStudentCourseScore)
        );
    }
}
