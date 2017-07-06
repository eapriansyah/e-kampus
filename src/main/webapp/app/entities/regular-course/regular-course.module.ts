import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    RegularCourseService,
    RegularCoursePopupService,
    RegularCourseComponent,
    RegularCourseDetailComponent,
    RegularCourseDialogComponent,
    RegularCoursePopupComponent,
    RegularCourseDeletePopupComponent,
    RegularCourseDeleteDialogComponent,
    regularCourseRoute,
    regularCoursePopupRoute,
    RegularCourseResolvePagingParams,
    
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
    ...regularCourseRoute,
    ...regularCoursePopupRoute,
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
        RegularCourseComponent,
        RegularCourseDetailComponent,
        
    ],
    declarations: [
        RegularCourseComponent,
        RegularCourseDetailComponent,
        RegularCourseDialogComponent,
        RegularCourseDeleteDialogComponent,
        RegularCoursePopupComponent,
        RegularCourseDeletePopupComponent,
        
    ],
    entryComponents: [
        RegularCourseComponent,
        RegularCourseDialogComponent,
        RegularCoursePopupComponent,
        RegularCourseDeleteDialogComponent,
        RegularCourseDeletePopupComponent,
        
    ],
    providers: [
        RegularCourseService,
        RegularCoursePopupService,
        RegularCourseResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusRegularCourseModule {}
