import { BaseEntity } from './../../shared';

export class Party implements BaseEntity {
    constructor(
        public id?: any,
        public idParty?: any,
        public name?: string,
    ) {
    }
}
