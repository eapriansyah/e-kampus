import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {WorkType} from './work-type.model';
import {WorkTypeService} from './work-type.service';

@Component({
    selector: 'jhi-work-type-detail',
    templateUrl: './work-type-detail.component.html'
})
export class WorkTypeDetailComponent implements OnInit, OnDestroy {

    workType: WorkType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private workTypeService: WorkTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInWorkTypes();
    }

    load(id) {
        this.workTypeService.find(id).subscribe((workType) => {
            this.workType = workType;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInWorkTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'workTypeListModification',
            (response) => this.load(this.workType.idWorkType)
        );
    }
}
