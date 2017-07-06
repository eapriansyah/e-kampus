import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {Degree} from './degree.model';
import {DegreeService} from './degree.service';

@Component({
    selector: 'jhi-degree-detail',
    templateUrl: './degree-detail.component.html'
})
export class DegreeDetailComponent implements OnInit, OnDestroy {

    degree: Degree;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private degreeService: DegreeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDegrees();
    }

    load(id) {
        this.degreeService.find(id).subscribe((degree) => {
            this.degree = degree;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDegrees() {
        this.eventSubscriber = this.eventManager.subscribe(
            'degreeListModification',
            (response) => this.load(this.degree.idDegree)
        );
    }
}
