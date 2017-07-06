import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProgramStudyComponent } from './program-study.component';
import { ProgramStudyDetailComponent } from './program-study-detail.component';
import { ProgramStudyPopupComponent } from './program-study-dialog.component';
import { ProgramStudyDeletePopupComponent } from './program-study-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ProgramStudyResolvePagingParams implements Resolve<any> {

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

export const programStudyRoute: Routes = [
    {
        path: 'program-study',
        component: ProgramStudyComponent,
        resolve: {
            'pagingParams': ProgramStudyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.programStudy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'program-study/:id',
        component: ProgramStudyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.programStudy.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const programStudyPopupRoute: Routes = [
    {
        path: 'program-study-new',
        component: ProgramStudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.programStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'program-study/:id/edit',
        component: ProgramStudyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.programStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'program-study/:id/delete',
        component: ProgramStudyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.programStudy.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
