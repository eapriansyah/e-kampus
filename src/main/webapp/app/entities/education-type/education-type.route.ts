import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EducationTypeComponent } from './education-type.component';
import { EducationTypeDetailComponent } from './education-type-detail.component';
import { EducationTypePopupComponent } from './education-type-dialog.component';
import { EducationTypeDeletePopupComponent } from './education-type-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class EducationTypeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idEducationType,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const educationTypeRoute: Routes = [
    {
        path: 'education-type',
        component: EducationTypeComponent,
        resolve: {
            'pagingParams': EducationTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.educationType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'education-type/:id',
        component: EducationTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.educationType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const educationTypePopupRoute: Routes = [
    {
        path: 'education-type-new',
        component: EducationTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.educationType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-type/:id/edit',
        component: EducationTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.educationType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'education-type/:id/delete',
        component: EducationTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.educationType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
