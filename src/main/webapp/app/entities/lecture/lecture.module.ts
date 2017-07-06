import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    LectureService,
    LecturePopupService,
    LectureComponent,
    LectureDetailComponent,
    LectureDialogComponent,
    LecturePopupComponent,
    LectureDeletePopupComponent,
    LectureDeleteDialogComponent,
    lectureRoute,
    lecturePopupRoute,
    LectureResolvePagingParams,
    
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
    ...lectureRoute,
    ...lecturePopupRoute,
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
        LectureComponent,
        LectureDetailComponent,
        
    ],
    declarations: [
        LectureComponent,
        LectureDetailComponent,
        LectureDialogComponent,
        LectureDeleteDialogComponent,
        LecturePopupComponent,
        LectureDeletePopupComponent,
        
    ],
    entryComponents: [
        LectureComponent,
        LectureDialogComponent,
        LecturePopupComponent,
        LectureDeleteDialogComponent,
        LectureDeletePopupComponent,
        
    ],
    providers: [
        LectureService,
        LecturePopupService,
        LectureResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusLectureModule {}
