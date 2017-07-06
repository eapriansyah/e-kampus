import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DatePipe } from '@angular/common';
import { OnGoingEvent } from './on-going-event.model';
import { OnGoingEventService } from './on-going-event.service';

@Injectable()
export class OnGoingEventPopupService {
    private isOpen = false;
    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private onGoingEventService: OnGoingEventService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.onGoingEventService.find(id).subscribe((onGoingEvent) => {
                onGoingEvent.dateFrom = this.datePipe
                    .transform(onGoingEvent.dateFrom, 'yyyy-MM-ddThh:mm');
                onGoingEvent.dateThru = this.datePipe
                    .transform(onGoingEvent.dateThru, 'yyyy-MM-ddThh:mm');
                this.onGoingEventModalRef(component, onGoingEvent);
            });
        } else {
            return this.onGoingEventModalRef(component, new OnGoingEvent());
        }
    }

    onGoingEventModalRef(component: Component, onGoingEvent: OnGoingEvent): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.onGoingEvent = onGoingEvent;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
