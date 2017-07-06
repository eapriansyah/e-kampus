import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ReligionTypeComponent } from './religion-type.component';
import { ReligionTypeDetailComponent } from './religion-type-detail.component';
import { ReligionTypePopupComponent } from './religion-type-dialog.component';
import { ReligionTypeDeletePopupComponent } from './religion-type-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ReligionTypeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idReligionType,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const religionTypeRoute: Routes = [
    {
        path: 'religion-type',
        component: ReligionTypeComponent,
        resolve: {
            'pagingParams': ReligionTypeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.religionType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'religion-type/:id',
        component: ReligionTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.religionType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const religionTypePopupRoute: Routes = [
    {
        path: 'religion-type-new',
        component: ReligionTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.religionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'religion-type/:id/edit',
        component: ReligionTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.religionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'religion-type/:id/delete',
        component: ReligionTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.religionType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
