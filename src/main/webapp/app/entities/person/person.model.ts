import { BaseEntity } from './../../shared';

export class Person implements BaseEntity {
    constructor(
        public id?: any,
        public idParty?: any,
        public firstName?: string,
        public lastName?: string,
        public pob?: string,
        public bloodType?: string,
        public gender?: string,
        public dob?: any,
    ) {
    }
}
