import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    AcademicPeriodsService,
    AcademicPeriodsPopupService,
    AcademicPeriodsComponent,
    AcademicPeriodsDetailComponent,
    AcademicPeriodsDialogComponent,
    AcademicPeriodsPopupComponent,
    AcademicPeriodsDeletePopupComponent,
    AcademicPeriodsDeleteDialogComponent,
    academicPeriodsRoute,
    academicPeriodsPopupRoute,
    AcademicPeriodsResolvePagingParams,
    AcademicPeriodsAsList,
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
    ...academicPeriodsRoute,
    ...academicPeriodsPopupRoute,
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
        AcademicPeriodsComponent,
        AcademicPeriodsDetailComponent,
        AcademicPeriodsAsList
    ],
    declarations: [
        AcademicPeriodsComponent,
        AcademicPeriodsDetailComponent,
        AcademicPeriodsDialogComponent,
        AcademicPeriodsDeleteDialogComponent,
        AcademicPeriodsPopupComponent,
        AcademicPeriodsDeletePopupComponent,
        AcademicPeriodsAsList
    ],
    entryComponents: [
        AcademicPeriodsComponent,
        AcademicPeriodsDialogComponent,
        AcademicPeriodsPopupComponent,
        AcademicPeriodsDeleteDialogComponent,
        AcademicPeriodsDeletePopupComponent,
        AcademicPeriodsAsList
    ],
    providers: [
        AcademicPeriodsService,
        AcademicPeriodsPopupService,
        AcademicPeriodsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusAcademicPeriodsModule {}
