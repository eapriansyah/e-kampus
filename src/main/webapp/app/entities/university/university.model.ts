import { BaseEntity } from './../../shared';

export class University implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idInternal?: string,
        public name?: string,
    ) {
    }
}
