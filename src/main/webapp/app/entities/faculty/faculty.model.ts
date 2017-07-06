import { BaseEntity } from './../../shared';

export class Faculty implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idInternal?: string,
        public name?: string,
        public universityId?: any,
    ) {
    }
}
