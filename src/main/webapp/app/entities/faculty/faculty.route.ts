import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { FacultyComponent } from './faculty.component';
import { FacultyDetailComponent } from './faculty-detail.component';
import { FacultyPopupComponent } from './faculty-dialog.component';
import { FacultyDeletePopupComponent } from './faculty-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class FacultyResolvePagingParams implements Resolve<any> {

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

export const facultyRoute: Routes = [
    {
        path: 'faculty',
        component: FacultyComponent,
        resolve: {
            'pagingParams': FacultyResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.faculty.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'faculty/:id',
        component: FacultyDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.faculty.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const facultyPopupRoute: Routes = [
    {
        path: 'faculty-new',
        component: FacultyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.faculty.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'faculty/:id/edit',
        component: FacultyPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.faculty.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'faculty/:id/delete',
        component: FacultyDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.faculty.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
