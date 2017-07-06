import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PostStudentService,
    PostStudentPopupService,
    PostStudentComponent,
    PostStudentDetailComponent,
    PostStudentDialogComponent,
    PostStudentPopupComponent,
    PostStudentDeletePopupComponent,
    PostStudentDeleteDialogComponent,
    postStudentRoute,
    postStudentPopupRoute,
    PostStudentResolvePagingParams,
    
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
    ...postStudentRoute,
    ...postStudentPopupRoute,
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
        PostStudentComponent,
        PostStudentDetailComponent,
        
    ],
    declarations: [
        PostStudentComponent,
        PostStudentDetailComponent,
        PostStudentDialogComponent,
        PostStudentDeleteDialogComponent,
        PostStudentPopupComponent,
        PostStudentDeletePopupComponent,
        
    ],
    entryComponents: [
        PostStudentComponent,
        PostStudentDialogComponent,
        PostStudentPopupComponent,
        PostStudentDeleteDialogComponent,
        PostStudentDeletePopupComponent,
        
    ],
    providers: [
        PostStudentService,
        PostStudentPopupService,
        PostStudentResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPostStudentModule {}
