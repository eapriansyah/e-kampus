import { BaseEntity } from './../../shared';

export class ProgramStudy implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public idInternal?: string,
        public name?: string,
        public status?: number,
        public degreeId?: any,
        public facultyId?: any,
    ) {
    }
}
