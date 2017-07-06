import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ProgramStudy} from './program-study.model';
import {ProgramStudyService} from './program-study.service';

@Component({
    selector: 'jhi-program-study-detail',
    templateUrl: './program-study-detail.component.html'
})
export class ProgramStudyDetailComponent implements OnInit, OnDestroy {

    programStudy: ProgramStudy;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private programStudyService: ProgramStudyService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInProgramStudies();
    }

    load(id) {
        this.programStudyService.find(id).subscribe((programStudy) => {
            this.programStudy = programStudy;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProgramStudies() {
        this.eventSubscriber = this.eventManager.subscribe(
            'programStudyListModification',
            (response) => this.load(this.programStudy.idPartyRole)
        );
    }
}
