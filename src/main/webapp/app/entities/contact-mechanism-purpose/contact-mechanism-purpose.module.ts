import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ContactMechanismPurposeService,
    ContactMechanismPurposePopupService,
    ContactMechanismPurposeComponent,
    ContactMechanismPurposeDetailComponent,
    ContactMechanismPurposeDialogComponent,
    ContactMechanismPurposePopupComponent,
    ContactMechanismPurposeDeletePopupComponent,
    ContactMechanismPurposeDeleteDialogComponent,
    contactMechanismPurposeRoute,
    contactMechanismPurposePopupRoute,
    ContactMechanismPurposeResolvePagingParams,
    ContactMechanismPurposeAsList,
} from './';

import {AutoCompleteModule,
        CheckboxModule,
        InputTextModule,
        CalendarModule,
        DropdownModule,
        ButtonModule,
        DataTableModule,
        PaginatorModule,
        ConfirmDialogModule,
        ConfirmationService,
        GrowlModule,
        DataGridModule,
        SharedModule,
        AccordionModule,
        TabViewModule,
        FieldsetModule,
        ScheduleModule,
        PanelModule,
        MenuItem,
        Header,
        Footer} from 'primeng/primeng';

const ENTITY_STATES = [
    ...contactMechanismPurposeRoute,
    ...contactMechanismPurposePopupRoute,
];

@NgModule({
    imports: [
        KampusSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true }),
        ButtonModule,
        DataTableModule,
        PaginatorModule,
        ConfirmDialogModule,
        TabViewModule,
        DataGridModule,
        ScheduleModule,
        AutoCompleteModule,
        CheckboxModule,
        CalendarModule,
        DropdownModule,
        FieldsetModule,
        PanelModule
    ],
    exports: [
        ContactMechanismPurposeComponent,
        ContactMechanismPurposeDetailComponent,
        ContactMechanismPurposeAsList
    ],
    declarations: [
        ContactMechanismPurposeComponent,
        ContactMechanismPurposeDetailComponent,
        ContactMechanismPurposeDialogComponent,
        ContactMechanismPurposeDeleteDialogComponent,
        ContactMechanismPurposePopupComponent,
        ContactMechanismPurposeDeletePopupComponent,
        ContactMechanismPurposeAsList
    ],
    entryComponents: [
        ContactMechanismPurposeComponent,
        ContactMechanismPurposeDialogComponent,
        ContactMechanismPurposePopupComponent,
        ContactMechanismPurposeDeleteDialogComponent,
        ContactMechanismPurposeDeletePopupComponent,
        ContactMechanismPurposeAsList
    ],
    providers: [
        ContactMechanismPurposeService,
        ContactMechanismPurposePopupService,
        ContactMechanismPurposeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusContactMechanismPurposeModule {}
