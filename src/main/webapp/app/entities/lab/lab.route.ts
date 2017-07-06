import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { LabComponent } from './lab.component';
import { LabDetailComponent } from './lab-detail.component';
import { LabPopupComponent } from './lab-dialog.component';
import { LabDeletePopupComponent } from './lab-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class LabResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idFacility,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const labRoute: Routes = [
    {
        path: 'lab',
        component: LabComponent,
        resolve: {
            'pagingParams': LabResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lab.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'lab/:id',
        component: LabDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lab.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const labPopupRoute: Routes = [
    {
        path: 'lab-new',
        component: LabPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lab.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lab/:id/edit',
        component: LabPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lab.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'lab/:id/delete',
        component: LabDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.lab.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
