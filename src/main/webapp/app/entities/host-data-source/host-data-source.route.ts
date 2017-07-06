import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { HostDataSourceComponent } from './host-data-source.component';
import { HostDataSourceDetailComponent } from './host-data-source-detail.component';
import { HostDataSourcePopupComponent } from './host-data-source-dialog.component';
import { HostDataSourceDeletePopupComponent } from './host-data-source-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class HostDataSourceResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idHostDataSource,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const hostDataSourceRoute: Routes = [
    {
        path: 'host-data-source',
        component: HostDataSourceComponent,
        resolve: {
            'pagingParams': HostDataSourceResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.hostDataSource.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'host-data-source/:id',
        component: HostDataSourceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.hostDataSource.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hostDataSourcePopupRoute: Routes = [
    {
        path: 'host-data-source-new',
        component: HostDataSourcePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.hostDataSource.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'host-data-source/:id/edit',
        component: HostDataSourcePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.hostDataSource.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'host-data-source/:id/delete',
        component: HostDataSourceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.hostDataSource.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
