import { BaseEntity } from './../../shared';

export class Degree implements BaseEntity {
    constructor(
        public id?: number,
        public idDegree?: number,
        public description?: string,
        public maxStudy?: number,
    ) {
    }
}
