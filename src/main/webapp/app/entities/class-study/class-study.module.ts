import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    ClassStudyService,
    ClassStudyPopupService,
    ClassStudyComponent,
    ClassStudyDetailComponent,
    ClassStudyDialogComponent,
    ClassStudyPopupComponent,
    ClassStudyDeletePopupComponent,
    ClassStudyDeleteDialogComponent,
    classStudyRoute,
    classStudyPopupRoute,
    ClassStudyResolvePagingParams,
    ClassStudyAsList,
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
    ...classStudyRoute,
    ...classStudyPopupRoute,
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
        ClassStudyComponent,
        ClassStudyDetailComponent,
        ClassStudyAsList
    ],
    declarations: [
        ClassStudyComponent,
        ClassStudyDetailComponent,
        ClassStudyDialogComponent,
        ClassStudyDeleteDialogComponent,
        ClassStudyPopupComponent,
        ClassStudyDeletePopupComponent,
        ClassStudyAsList
    ],
    entryComponents: [
        ClassStudyComponent,
        ClassStudyDialogComponent,
        ClassStudyPopupComponent,
        ClassStudyDeleteDialogComponent,
        ClassStudyDeletePopupComponent,
        ClassStudyAsList
    ],
    providers: [
        ClassStudyService,
        ClassStudyPopupService,
        ClassStudyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusClassStudyModule {}
