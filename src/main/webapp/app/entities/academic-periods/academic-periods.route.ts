import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AcademicPeriodsComponent } from './academic-periods.component';
import { AcademicPeriodsDetailComponent } from './academic-periods-detail.component';
import { AcademicPeriodsPopupComponent } from './academic-periods-dialog.component';
import { AcademicPeriodsDeletePopupComponent } from './academic-periods-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class AcademicPeriodsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idPeriod,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const academicPeriodsRoute: Routes = [
    {
        path: 'academic-periods',
        component: AcademicPeriodsComponent,
        resolve: {
            'pagingParams': AcademicPeriodsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.academicPeriods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'academic-periods/:id',
        component: AcademicPeriodsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.academicPeriods.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const academicPeriodsPopupRoute: Routes = [
    {
        path: 'academic-periods-new',
        component: AcademicPeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.academicPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'academic-periods/:id/edit',
        component: AcademicPeriodsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.academicPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'academic-periods/:id/delete',
        component: AcademicPeriodsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.academicPeriods.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
