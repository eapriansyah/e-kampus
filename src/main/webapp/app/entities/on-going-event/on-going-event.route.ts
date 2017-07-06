import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { OnGoingEventComponent } from './on-going-event.component';
import { OnGoingEventDetailComponent } from './on-going-event-detail.component';
import { OnGoingEventPopupComponent } from './on-going-event-dialog.component';
import { OnGoingEventDeletePopupComponent } from './on-going-event-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class OnGoingEventResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idEventGo,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const onGoingEventRoute: Routes = [
    {
        path: 'on-going-event',
        component: OnGoingEventComponent,
        resolve: {
            'pagingParams': OnGoingEventResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.onGoingEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'on-going-event/:id',
        component: OnGoingEventDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.onGoingEvent.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const onGoingEventPopupRoute: Routes = [
    {
        path: 'on-going-event-new',
        component: OnGoingEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.onGoingEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'on-going-event/:id/edit',
        component: OnGoingEventPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.onGoingEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'on-going-event/:id/delete',
        component: OnGoingEventDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.onGoingEvent.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
