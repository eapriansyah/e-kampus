import { BaseEntity } from './../../shared';

export class Internal implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idInternal?: string,
        public name?: string,
        public description?: string,
        public idRoleType?: number,
        public idStatusType?: number,
    ) {
    }
}
