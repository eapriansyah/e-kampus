import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PurposeTypeService,
    PurposeTypePopupService,
    PurposeTypeComponent,
    PurposeTypeDetailComponent,
    PurposeTypeDialogComponent,
    PurposeTypePopupComponent,
    PurposeTypeDeletePopupComponent,
    PurposeTypeDeleteDialogComponent,
    purposeTypeRoute,
    purposeTypePopupRoute,
    PurposeTypeResolvePagingParams,
    
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
    ...purposeTypeRoute,
    ...purposeTypePopupRoute,
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
        PurposeTypeComponent,
        PurposeTypeDetailComponent,
        
    ],
    declarations: [
        PurposeTypeComponent,
        PurposeTypeDetailComponent,
        PurposeTypeDialogComponent,
        PurposeTypeDeleteDialogComponent,
        PurposeTypePopupComponent,
        PurposeTypeDeletePopupComponent,
        
    ],
    entryComponents: [
        PurposeTypeComponent,
        PurposeTypeDialogComponent,
        PurposeTypePopupComponent,
        PurposeTypeDeleteDialogComponent,
        PurposeTypeDeletePopupComponent,
        
    ],
    providers: [
        PurposeTypeService,
        PurposeTypePopupService,
        PurposeTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPurposeTypeModule {}
