import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PeriodTypeComponent } from './period-type.component';
import { PeriodTypeDetailComponent } from './period-type-detail.component';
import { PeriodTypePopupComponent } from './period-type-dialog.component';
import { PeriodTypeDeletePopupComponent } from './period-type-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PeriodTypeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPeriodType,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const periodTypeRoute: Routes = [
    {
        path: 'period-type',
        component: PeriodTypeComponent,
        resolve: {
            'pagingParams': PeriodTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.periodType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'period-type/:id',
        component: PeriodTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.periodType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const periodTypePopupRoute: Routes = [
    {
        path: 'period-type-new',
        component: PeriodTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.periodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'period-type/:id/edit',
        component: PeriodTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.periodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'period-type/:id/delete',
        component: PeriodTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.periodType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
