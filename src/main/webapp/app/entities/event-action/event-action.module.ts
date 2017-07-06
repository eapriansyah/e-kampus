import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    EventActionService,
    EventActionPopupService,
    EventActionComponent,
    EventActionDetailComponent,
    EventActionDialogComponent,
    EventActionPopupComponent,
    EventActionDeletePopupComponent,
    EventActionDeleteDialogComponent,
    eventActionRoute,
    eventActionPopupRoute,
    EventActionResolvePagingParams,
    
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
    ...eventActionRoute,
    ...eventActionPopupRoute,
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
        EventActionComponent,
        EventActionDetailComponent,
        
    ],
    declarations: [
        EventActionComponent,
        EventActionDetailComponent,
        EventActionDialogComponent,
        EventActionDeleteDialogComponent,
        EventActionPopupComponent,
        EventActionDeletePopupComponent,
        
    ],
    entryComponents: [
        EventActionComponent,
        EventActionDialogComponent,
        EventActionPopupComponent,
        EventActionDeleteDialogComponent,
        EventActionDeletePopupComponent,
        
    ],
    providers: [
        EventActionService,
        EventActionPopupService,
        EventActionResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusEventActionModule {}
