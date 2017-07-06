import { BaseEntity } from './../../shared';

export class Organization implements BaseEntity {
    constructor(
        public id?: any,
        public idParty?: any,
        public name?: string,
    ) {
    }
}
