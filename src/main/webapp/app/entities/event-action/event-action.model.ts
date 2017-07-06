import { BaseEntity } from './../../shared';

export class EventAction implements BaseEntity {
    constructor(
        public id?: number,
        public idEventAction?: number,
        public description?: string,
    ) {
    }
}
