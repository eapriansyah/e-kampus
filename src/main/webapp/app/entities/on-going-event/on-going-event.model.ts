import { BaseEntity } from './../../shared';

export class OnGoingEvent implements BaseEntity {
    constructor(
        public id?: any,
        public idEventGo?: any,
        public idEvent?: string,
        public description?: string,
        public dateFrom?: any,
        public dateThru?: any,
        public ownerId?: any,
        public periodId?: any,
        public eventId?: any,
    ) {
    }
}
