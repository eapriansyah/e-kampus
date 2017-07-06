import { BaseEntity } from './../../shared';

export class EducationType implements BaseEntity {
    constructor(
        public id?: number,
        public idEducationType?: number,
        public description?: string,
    ) {
    }
}
