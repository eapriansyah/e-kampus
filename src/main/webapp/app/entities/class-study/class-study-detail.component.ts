import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ClassStudy} from './class-study.model';
import {ClassStudyService} from './class-study.service';

@Component({
    selector: 'jhi-class-study-detail',
    templateUrl: './class-study-detail.component.html'
})
export class ClassStudyDetailComponent implements OnInit, OnDestroy {

    classStudy: ClassStudy;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private classStudyService: ClassStudyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInClassStudies();
    }

    load(id) {
        this.classStudyService.find(id).subscribe((classStudy) => {
            this.classStudy = classStudy;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInClassStudies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'classStudyListModification',
            (response) => this.load(this.classStudy.idClassStudy)
        );
    }
}
