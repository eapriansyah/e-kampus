import { BaseEntity } from './../../shared';

export class PostalAddress implements BaseEntity {
    constructor(
        public id?: any,
        public idContact?: any,
        public address1?: string,
        public address2?: string,
        public city?: string,
        public province?: string,
    ) {
    }
}
