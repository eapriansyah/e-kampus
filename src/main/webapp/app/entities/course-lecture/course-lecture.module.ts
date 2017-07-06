import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    CourseLectureService,
    CourseLecturePopupService,
    CourseLectureComponent,
    CourseLectureDetailComponent,
    CourseLectureDialogComponent,
    CourseLecturePopupComponent,
    CourseLectureDeletePopupComponent,
    CourseLectureDeleteDialogComponent,
    courseLectureRoute,
    courseLecturePopupRoute,
    CourseLectureResolvePagingParams,
    
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
    ...courseLectureRoute,
    ...courseLecturePopupRoute,
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
        CourseLectureComponent,
        CourseLectureDetailComponent,
        
    ],
    declarations: [
        CourseLectureComponent,
        CourseLectureDetailComponent,
        CourseLectureDialogComponent,
        CourseLectureDeleteDialogComponent,
        CourseLecturePopupComponent,
        CourseLectureDeletePopupComponent,
        
    ],
    entryComponents: [
        CourseLectureComponent,
        CourseLectureDialogComponent,
        CourseLecturePopupComponent,
        CourseLectureDeleteDialogComponent,
        CourseLectureDeletePopupComponent,
        
    ],
    providers: [
        CourseLectureService,
        CourseLecturePopupService,
        CourseLectureResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusCourseLectureModule {}
