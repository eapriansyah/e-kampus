import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    OnGoingEventService,
    OnGoingEventPopupService,
    OnGoingEventComponent,
    OnGoingEventDetailComponent,
    OnGoingEventDialogComponent,
    OnGoingEventPopupComponent,
    OnGoingEventDeletePopupComponent,
    OnGoingEventDeleteDialogComponent,
    onGoingEventRoute,
    onGoingEventPopupRoute,
    OnGoingEventResolvePagingParams,
    
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
    ...onGoingEventRoute,
    ...onGoingEventPopupRoute,
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
        OnGoingEventComponent,
        OnGoingEventDetailComponent,
        
    ],
    declarations: [
        OnGoingEventComponent,
        OnGoingEventDetailComponent,
        OnGoingEventDialogComponent,
        OnGoingEventDeleteDialogComponent,
        OnGoingEventPopupComponent,
        OnGoingEventDeletePopupComponent,
        
    ],
    entryComponents: [
        OnGoingEventComponent,
        OnGoingEventDialogComponent,
        OnGoingEventPopupComponent,
        OnGoingEventDeleteDialogComponent,
        OnGoingEventDeletePopupComponent,
        
    ],
    providers: [
        OnGoingEventService,
        OnGoingEventPopupService,
        OnGoingEventResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusOnGoingEventModule {}
