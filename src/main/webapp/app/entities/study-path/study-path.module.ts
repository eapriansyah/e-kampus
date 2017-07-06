import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    StudyPathService,
    StudyPathPopupService,
    StudyPathComponent,
    StudyPathDetailComponent,
    StudyPathDialogComponent,
    StudyPathPopupComponent,
    StudyPathDeletePopupComponent,
    StudyPathDeleteDialogComponent,
    studyPathRoute,
    studyPathPopupRoute,
    StudyPathResolvePagingParams,
    
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
    ...studyPathRoute,
    ...studyPathPopupRoute,
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
        StudyPathComponent,
        StudyPathDetailComponent,
        
    ],
    declarations: [
        StudyPathComponent,
        StudyPathDetailComponent,
        StudyPathDialogComponent,
        StudyPathDeleteDialogComponent,
        StudyPathPopupComponent,
        StudyPathDeletePopupComponent,
        
    ],
    entryComponents: [
        StudyPathComponent,
        StudyPathDialogComponent,
        StudyPathPopupComponent,
        StudyPathDeleteDialogComponent,
        StudyPathDeletePopupComponent,
        
    ],
    providers: [
        StudyPathService,
        StudyPathPopupService,
        StudyPathResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusStudyPathModule {}
