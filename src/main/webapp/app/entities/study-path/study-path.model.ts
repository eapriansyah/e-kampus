import { BaseEntity } from './../../shared';

export class StudyPath implements BaseEntity {
    constructor(
        public id?: number,
        public idStudyPath?: number,
        public description?: string,
    ) {
    }
}
