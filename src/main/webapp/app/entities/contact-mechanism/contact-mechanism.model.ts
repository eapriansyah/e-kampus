import { BaseEntity } from './../../shared';

export class ContactMechanism implements BaseEntity {
    constructor(
        public id?: any,
        public idContact?: any,
        public description?: string,
    ) {
    }
}
