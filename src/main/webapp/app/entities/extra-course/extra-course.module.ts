import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ExtraCourseService,
    ExtraCoursePopupService,
    ExtraCourseComponent,
    ExtraCourseDetailComponent,
    ExtraCourseDialogComponent,
    ExtraCoursePopupComponent,
    ExtraCourseDeletePopupComponent,
    ExtraCourseDeleteDialogComponent,
    extraCourseRoute,
    extraCoursePopupRoute,
    ExtraCourseResolvePagingParams,
    
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
    ...extraCourseRoute,
    ...extraCoursePopupRoute,
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
        ExtraCourseComponent,
        ExtraCourseDetailComponent,
        
    ],
    declarations: [
        ExtraCourseComponent,
        ExtraCourseDetailComponent,
        ExtraCourseDialogComponent,
        ExtraCourseDeleteDialogComponent,
        ExtraCoursePopupComponent,
        ExtraCourseDeletePopupComponent,
        
    ],
    entryComponents: [
        ExtraCourseComponent,
        ExtraCourseDialogComponent,
        ExtraCoursePopupComponent,
        ExtraCourseDeleteDialogComponent,
        ExtraCourseDeletePopupComponent,
        
    ],
    providers: [
        ExtraCourseService,
        ExtraCoursePopupService,
        ExtraCourseResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusExtraCourseModule {}
