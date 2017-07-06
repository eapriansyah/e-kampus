import { BaseEntity } from './../../shared';

export class StudentEvent implements BaseEntity {
    constructor(
        public id?: number,
        public idStudentEvent?: number,
        public idEvent?: string,
        public description?: string,
        public registrationtype?: number,
        public actionDoneId?: number,
        public actionFailedId?: number,
    ) {
    }
}
