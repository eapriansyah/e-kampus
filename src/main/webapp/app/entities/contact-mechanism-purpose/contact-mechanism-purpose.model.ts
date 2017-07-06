import { BaseEntity } from './../../shared';

export class ContactMechanismPurpose implements BaseEntity {
    constructor(
        public id?: any,
        public idContactMechPurpose?: any,
        public dateFrom?: any,
        public dateThru?: any,
        public purposeId?: any,
        public partyId?: any,
        public contactId?: any,
    ) {
    }
}
