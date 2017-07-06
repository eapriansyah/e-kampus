import { BaseEntity } from './../../shared';

export class ExtraCourse implements BaseEntity {
    constructor(
        public id?: any,
        public idCourse?: any,
        public courseCode?: string,
        public description?: string,
        public credit?: number,
        public ownerId?: any,
    ) {
    }
}
