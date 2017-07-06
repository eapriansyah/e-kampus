import { BaseEntity } from './../../shared';

export class TelecomunicationNumber implements BaseEntity {
    constructor(
        public id?: any,
        public idContact?: any,
        public number?: string,
    ) {
    }
}
