import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ContactMechanismPurposeComponent } from './contact-mechanism-purpose.component';
import { ContactMechanismPurposeDetailComponent } from './contact-mechanism-purpose-detail.component';
import { ContactMechanismPurposePopupComponent } from './contact-mechanism-purpose-dialog.component';
import { ContactMechanismPurposeDeletePopupComponent } from './contact-mechanism-purpose-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ContactMechanismPurposeResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'idContactMechPurpose,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const contactMechanismPurposeRoute: Routes = [
    {
        path: 'contact-mechanism-purpose',
        component: ContactMechanismPurposeComponent,
        resolve: {
            'pagingParams': ContactMechanismPurposeResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanismPurpose.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contact-mechanism-purpose/:id',
        component: ContactMechanismPurposeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanismPurpose.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contactMechanismPurposePopupRoute: Routes = [
    {
        path: 'contact-mechanism-purpose-new',
        component: ContactMechanismPurposePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanismPurpose.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-mechanism-purpose/:id/edit',
        component: ContactMechanismPurposePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanismPurpose.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-mechanism-purpose/:id/delete',
        component: ContactMechanismPurposeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'kampusApp.contactMechanismPurpose.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
