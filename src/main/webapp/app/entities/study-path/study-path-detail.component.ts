import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudyPath} from './study-path.model';
import {StudyPathService} from './study-path.service';

@Component({
    selector: 'jhi-study-path-detail',
    templateUrl: './study-path-detail.component.html'
})
export class StudyPathDetailComponent implements OnInit, OnDestroy {

    studyPath: StudyPath;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studyPathService: StudyPathService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudyPaths();
    }

    load(id) {
        this.studyPathService.find(id).subscribe((studyPath) => {
            this.studyPath = studyPath;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudyPaths() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studyPathListModification',
            (response) => this.load(this.studyPath.idStudyPath)
        );
    }
}
