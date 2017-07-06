import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EventActionComponent } from './event-action.component';
import { EventActionDetailComponent } from './event-action-detail.component';
import { EventActionPopupComponent } from './event-action-dialog.component';
import { EventActionDeletePopupComponent } from './event-action-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class EventActionResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idEventAction,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const eventActionRoute: Routes = [
    {
        path: 'event-action',
        component: EventActionComponent,
        resolve: {
            'pagingParams': EventActionResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.eventAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'event-action/:id',
        component: EventActionDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.eventAction.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const eventActionPopupRoute: Routes = [
    {
        path: 'event-action-new',
        component: EventActionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.eventAction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'event-action/:id/edit',
        component: EventActionPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.eventAction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'event-action/:id/delete',
        component: EventActionDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.eventAction.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
