import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ContactMechanismComponent } from './contact-mechanism.component';
import { ContactMechanismDetailComponent } from './contact-mechanism-detail.component';
import { ContactMechanismPopupComponent } from './contact-mechanism-dialog.component';
import { ContactMechanismDeletePopupComponent } from './contact-mechanism-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ContactMechanismResolvePagingParams implements Resolve<any> {

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

export const contactMechanismRoute: Routes = [
    {
        path: 'contact-mechanism',
        component: ContactMechanismComponent,
        resolve: {
            'pagingParams': ContactMechanismResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanism.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contact-mechanism/:id',
        component: ContactMechanismDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanism.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contactMechanismPopupRoute: Routes = [
    {
        path: 'contact-mechanism-new',
        component: ContactMechanismPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanism.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-mechanism/:id/edit',
        component: ContactMechanismPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanism.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-mechanism/:id/delete',
        component: ContactMechanismDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanism.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
