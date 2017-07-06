import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    CourseApplicableService,
    CourseApplicablePopupService,
    CourseApplicableComponent,
    CourseApplicableDetailComponent,
    CourseApplicableDialogComponent,
    CourseApplicablePopupComponent,
    CourseApplicableDeletePopupComponent,
    CourseApplicableDeleteDialogComponent,
    courseApplicableRoute,
    courseApplicablePopupRoute,
    CourseApplicableResolvePagingParams,
    CourseApplicableAsList,
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
    ...courseApplicableRoute,
    ...courseApplicablePopupRoute,
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
        CourseApplicableComponent,
        CourseApplicableDetailComponent,
        CourseApplicableAsList
    ],
    declarations: [
        CourseApplicableComponent,
        CourseApplicableDetailComponent,
        CourseApplicableDialogComponent,
        CourseApplicableDeleteDialogComponent,
        CourseApplicablePopupComponent,
        CourseApplicableDeletePopupComponent,
        CourseApplicableAsList
    ],
    entryComponents: [
        CourseApplicableComponent,
        CourseApplicableDialogComponent,
        CourseApplicablePopupComponent,
        CourseApplicableDeleteDialogComponent,
        CourseApplicableDeletePopupComponent,
        CourseApplicableAsList
    ],
    providers: [
        CourseApplicableService,
        CourseApplicablePopupService,
        CourseApplicableResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusCourseApplicableModule {}
