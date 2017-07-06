import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PeriodTypeService,
    PeriodTypePopupService,
    PeriodTypeComponent,
    PeriodTypeDetailComponent,
    PeriodTypeDialogComponent,
    PeriodTypePopupComponent,
    PeriodTypeDeletePopupComponent,
    PeriodTypeDeleteDialogComponent,
    periodTypeRoute,
    periodTypePopupRoute,
    PeriodTypeResolvePagingParams,
    
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
    ...periodTypeRoute,
    ...periodTypePopupRoute,
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
        PeriodTypeComponent,
        PeriodTypeDetailComponent,
        
    ],
    declarations: [
        PeriodTypeComponent,
        PeriodTypeDetailComponent,
        PeriodTypeDialogComponent,
        PeriodTypeDeleteDialogComponent,
        PeriodTypePopupComponent,
        PeriodTypeDeletePopupComponent,
        
    ],
    entryComponents: [
        PeriodTypeComponent,
        PeriodTypeDialogComponent,
        PeriodTypePopupComponent,
        PeriodTypeDeleteDialogComponent,
        PeriodTypeDeletePopupComponent,
        
    ],
    providers: [
        PeriodTypeService,
        PeriodTypePopupService,
        PeriodTypeResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPeriodTypeModule {}
