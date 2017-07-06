import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudentPeriodDataService,
    StudentPeriodDataPopupService,
    StudentPeriodDataComponent,
    StudentPeriodDataDetailComponent,
    StudentPeriodDataDialogComponent,
    StudentPeriodDataPopupComponent,
    StudentPeriodDataDeletePopupComponent,
    StudentPeriodDataDeleteDialogComponent,
    studentPeriodDataRoute,
    studentPeriodDataPopupRoute,
    StudentPeriodDataResolvePagingParams,
    StudentPeriodDataAsList,
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
    ...studentPeriodDataRoute,
    ...studentPeriodDataPopupRoute,
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
        StudentPeriodDataComponent,
        StudentPeriodDataDetailComponent,
        StudentPeriodDataAsList
    ],
    declarations: [
        StudentPeriodDataComponent,
        StudentPeriodDataDetailComponent,
        StudentPeriodDataDialogComponent,
        StudentPeriodDataDeleteDialogComponent,
        StudentPeriodDataPopupComponent,
        StudentPeriodDataDeletePopupComponent,
        StudentPeriodDataAsList
    ],
    entryComponents: [
        StudentPeriodDataComponent,
        StudentPeriodDataDialogComponent,
        StudentPeriodDataPopupComponent,
        StudentPeriodDataDeleteDialogComponent,
        StudentPeriodDataDeletePopupComponent,
        StudentPeriodDataAsList
    ],
    providers: [
        StudentPeriodDataService,
        StudentPeriodDataPopupService,
        StudentPeriodDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudentPeriodDataModule {}
