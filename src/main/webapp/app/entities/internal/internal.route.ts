import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { InternalComponent } from './internal.component';
import { InternalDetailComponent } from './internal-detail.component';
import { InternalPopupComponent } from './internal-dialog.component';
import { InternalDeletePopupComponent } from './internal-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class InternalResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPartyRole,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const internalRoute: Routes = [
    {
        path: 'internal',
        component: InternalComponent,
        resolve: {
            'pagingParams': InternalResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.internal.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'internal/:id',
        component: InternalDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.internal.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const internalPopupRoute: Routes = [
    {
        path: 'internal-new',
        component: InternalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.internal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internal/:id/edit',
        component: InternalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.internal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'internal/:id/delete',
        component: InternalDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.internal.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
