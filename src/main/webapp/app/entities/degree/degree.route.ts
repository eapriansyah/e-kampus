import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { DegreeComponent } from './degree.component';
import { DegreeDetailComponent } from './degree-detail.component';
import { DegreePopupComponent } from './degree-dialog.component';
import { DegreeDeletePopupComponent } from './degree-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class DegreeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idDegree,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const degreeRoute: Routes = [
    {
        path: 'degree',
        component: DegreeComponent,
        resolve: {
            'pagingParams': DegreeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.degree.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'degree/:id',
        component: DegreeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.degree.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const degreePopupRoute: Routes = [
    {
        path: 'degree-new',
        component: DegreePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.degree.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'degree/:id/edit',
        component: DegreePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.degree.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'degree/:id/delete',
        component: DegreeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.degree.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
