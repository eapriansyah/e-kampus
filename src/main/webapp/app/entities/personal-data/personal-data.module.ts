import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PersonalDataService,
    PersonalDataPopupService,
    PersonalDataComponent,
    PersonalDataDetailComponent,
    PersonalDataDialogComponent,
    PersonalDataPopupComponent,
    PersonalDataDeletePopupComponent,
    PersonalDataDeleteDialogComponent,
    personalDataRoute,
    personalDataPopupRoute,
    PersonalDataResolvePagingParams,
    
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
    ...personalDataRoute,
    ...personalDataPopupRoute,
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
        PersonalDataComponent,
        PersonalDataDetailComponent,
        
    ],
    declarations: [
        PersonalDataComponent,
        PersonalDataDetailComponent,
        PersonalDataDialogComponent,
        PersonalDataDeleteDialogComponent,
        PersonalDataPopupComponent,
        PersonalDataDeletePopupComponent,
        
    ],
    entryComponents: [
        PersonalDataComponent,
        PersonalDataDialogComponent,
        PersonalDataPopupComponent,
        PersonalDataDeleteDialogComponent,
        PersonalDataDeletePopupComponent,
        
    ],
    providers: [
        PersonalDataService,
        PersonalDataPopupService,
        PersonalDataResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPersonalDataModule {}
