import { BaseEntity } from './../../shared';

export class Lecture implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idLecture?: string,
        public name?: string,
        public status?: number,
    ) {
    }
}
