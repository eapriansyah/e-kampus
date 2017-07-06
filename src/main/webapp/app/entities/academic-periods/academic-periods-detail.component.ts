import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {AcademicPeriods} from './academic-periods.model';
import {AcademicPeriodsService} from './academic-periods.service';

@Component({
    selector: 'jhi-academic-periods-detail',
    templateUrl: './academic-periods-detail.component.html'
})
export class AcademicPeriodsDetailComponent implements OnInit, OnDestroy {

    academicPeriods: AcademicPeriods;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private academicPeriodsService: AcademicPeriodsService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAcademicPeriods();
    }

    load(id) {
        this.academicPeriodsService.find(id).subscribe((academicPeriods) => {
            this.academicPeriods = academicPeriods;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAcademicPeriods() {
        this.eventSubscriber = this.eventManager.subscribe(
            'academicPeriodsListModification',
            (response) => this.load(this.academicPeriods.idPeriod)
        );
    }
}
