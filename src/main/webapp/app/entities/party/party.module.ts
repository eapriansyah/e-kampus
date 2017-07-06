import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PartyService,
    PartyPopupService,
    PartyComponent,
    PartyDetailComponent,
    PartyDialogComponent,
    PartyPopupComponent,
    PartyDeletePopupComponent,
    PartyDeleteDialogComponent,
    partyRoute,
    partyPopupRoute,
    PartyResolvePagingParams,
    
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
    ...partyRoute,
    ...partyPopupRoute,
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
        PartyComponent,
        PartyDetailComponent,
        
    ],
    declarations: [
        PartyComponent,
        PartyDetailComponent,
        PartyDialogComponent,
        PartyDeleteDialogComponent,
        PartyPopupComponent,
        PartyDeletePopupComponent,
        
    ],
    entryComponents: [
        PartyComponent,
        PartyDialogComponent,
        PartyPopupComponent,
        PartyDeleteDialogComponent,
        PartyDeletePopupComponent,
        
    ],
    providers: [
        PartyService,
        PartyPopupService,
        PartyResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPartyModule {}
