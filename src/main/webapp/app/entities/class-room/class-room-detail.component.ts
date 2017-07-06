import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import {ClassRoom} from './class-room.model';
import {ClassRoomService} from './class-room.service';

@Component({
    selector: 'jhi-class-room-detail',
    templateUrl: './class-room-detail.component.html'
})
export class ClassRoomDetailComponent implements OnInit, OnDestroy {

    classRoom: ClassRoom;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private classRoomService: ClassRoomService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInClassRooms();
    }

    load(id) {
        this.classRoomService.find(id).subscribe((classRoom) => {
            this.classRoom = classRoom;
        });
    }

    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInClassRooms() {
        this.eventSubscriber = this.eventManager.subscribe(
            'classRoomListModification',
            (response) => this.load(this.classRoom.idFacility)
        );
    }
}
