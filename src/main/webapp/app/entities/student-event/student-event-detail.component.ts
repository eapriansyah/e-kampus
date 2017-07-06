import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {StudentEvent} from './student-event.model';
import {StudentEventService} from './student-event.service';

@Component({
    selector: 'jhi-student-event-detail',
    templateUrl: './student-event-detail.component.html'
})
export class StudentEventDetailComponent implements OnInit, OnDestroy {

    studentEvent: StudentEvent;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private studentEventService: StudentEventService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStudentEvents();
    }

    load(id) {
        this.studentEventService.find(id).subscribe((studentEvent) => {
            this.studentEvent = studentEvent;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStudentEvents() {
        this.eventSubscriber = this.eventManager.subscribe(
            'studentEventListModification',
            (response) => this.load(this.studentEvent.idStudentEvent)
        );
    }
}
