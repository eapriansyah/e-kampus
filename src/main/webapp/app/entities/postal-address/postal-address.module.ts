import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { KampusSharedModule } from '../../shared';
import {
    PostalAddressService,
    PostalAddressPopupService,
    PostalAddressComponent,
    PostalAddressDetailComponent,
    PostalAddressDialogComponent,
    PostalAddressPopupComponent,
    PostalAddressDeletePopupComponent,
    PostalAddressDeleteDialogComponent,
    postalAddressRoute,
    postalAddressPopupRoute,
    PostalAddressResolvePagingParams,
    
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
    ...postalAddressRoute,
    ...postalAddressPopupRoute,
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
        PostalAddressComponent,
        PostalAddressDetailComponent,
        
    ],
    declarations: [
        PostalAddressComponent,
        PostalAddressDetailComponent,
        PostalAddressDialogComponent,
        PostalAddressDeleteDialogComponent,
        PostalAddressPopupComponent,
        PostalAddressDeletePopupComponent,
        
    ],
    entryComponents: [
        PostalAddressComponent,
        PostalAddressDialogComponent,
        PostalAddressPopupComponent,
        PostalAddressDeleteDialogComponent,
        PostalAddressDeletePopupComponent,
        
    ],
    providers: [
        PostalAddressService,
        PostalAddressPopupService,
        PostalAddressResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class KampusPostalAddressModule {}
