import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ElectronicAddressComponent } from './electronic-address.component';
import { ElectronicAddressDetailComponent } from './electronic-address-detail.component';
import { ElectronicAddressPopupComponent } from './electronic-address-dialog.component';
import { ElectronicAddressDeletePopupComponent } from './electronic-address-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ElectronicAddressResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idContact,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const electronicAddressRoute: Routes = [
    {
        path: 'electronic-address',
        component: ElectronicAddressComponent,
        resolve: {
            'pagingParams': ElectronicAddressResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.electronicAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'electronic-address/:id',
        component: ElectronicAddressDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.electronicAddress.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const electronicAddressPopupRoute: Routes = [
    {
        path: 'electronic-address-new',
        component: ElectronicAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.electronicAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'electronic-address/:id/edit',
        component: ElectronicAddressPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.electronicAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'electronic-address/:id/delete',
        component: ElectronicAddressDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.electronicAddress.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
