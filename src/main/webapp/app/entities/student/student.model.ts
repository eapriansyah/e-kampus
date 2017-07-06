import { BaseEntity } from './../../shared';

export class Student implements BaseEntity {
    constructor(
        public id?: any,
        public idPartyRole?: any,
        public nim?: string,
        public name?: string,
        public classof?: number,
        public status?: number,
        public prodyId?: any,
        public studyPathId?: any,
    ) {
    }
}
