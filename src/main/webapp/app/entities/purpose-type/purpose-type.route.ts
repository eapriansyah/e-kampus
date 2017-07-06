import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { PurposeTypeComponent } from './purpose-type.component';
import { PurposeTypeDetailComponent } from './purpose-type-detail.component';
import { PurposeTypePopupComponent } from './purpose-type-dialog.component';
import { PurposeTypeDeletePopupComponent } from './purpose-type-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class PurposeTypeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPurposeType,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const purposeTypeRoute: Routes = [
    {
        path: 'purpose-type',
        component: PurposeTypeComponent,
        resolve: {
            'pagingParams': PurposeTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.purposeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'purpose-type/:id',
        component: PurposeTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.purposeType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const purposeTypePopupRoute: Routes = [
    {
        path: 'purpose-type-new',
        component: PurposeTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.purposeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purpose-type/:id/edit',
        component: PurposeTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.purposeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'purpose-type/:id/delete',
        component: PurposeTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.purposeType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
