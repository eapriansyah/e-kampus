import { BaseEntity } from './../../shared';

export class PersonalData implements BaseEntity {
    constructor(
        public id?: any,
        public idPersonalData?: any,
        public nisn?: string,
        public motherName?: string,
        public fatherName?: string,
        public fatherDob?: any,
        public motherDob?: any,
        public hasPaud?: number,
        public hasTk?: number,
        public personId?: any,
        public motherReligionId?: any,
        public fatherReligionId?: any,
        public fatherEducationId?: any,
        public motherEducationId?: any,
        public fatherWorkTypeId?: any,
        public motherWorkTypeId?: any,
    ) {
    }
}
