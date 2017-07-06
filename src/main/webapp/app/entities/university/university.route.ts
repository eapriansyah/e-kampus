import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UniversityComponent } from './university.component';
import { UniversityDetailComponent } from './university-detail.component';
import { UniversityPopupComponent } from './university-dialog.component';
import { UniversityDeletePopupComponent } from './university-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class UniversityResolvePagingParams implements Resolve<any> {

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

export const universityRoute: Routes = [
    {
        path: 'university',
        component: UniversityComponent,
        resolve: {
            'pagingParams': UniversityResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.university.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'university/:id',
        component: UniversityDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.university.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const universityPopupRoute: Routes = [
    {
        path: 'university-new',
        component: UniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'university/:id/edit',
        component: UniversityPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'university/:id/delete',
        component: UniversityDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.university.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
