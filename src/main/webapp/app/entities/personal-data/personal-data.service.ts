import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { PersonalData } from './personal-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PersonalDataService {

    private resourceUrl = 'api/personal-data';
    private resourceSearchUrl = 'api/_search/personal-data';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(personalData: PersonalData): Observable<PersonalData> {
        const copy = this.convert(personalData);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(personalData: PersonalData): Observable<PersonalData> {
        const copy = this.convert(personalData);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: any): Observable<PersonalData> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }


    delete(id?: any): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

	executeProcess(personalData: any): Observable<String> {
        const copy = this.convert(personalData);
        return this.http.post(this.resourceUrl + '/execute', copy).map((res: Response) => {
            return res.json();
        });
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.fatherDob = this.dateUtils
            .convertLocalDateFromServer(entity.fatherDob);
        entity.motherDob = this.dateUtils
            .convertLocalDateFromServer(entity.motherDob);
    }

    private convert(personalData: PersonalData): PersonalData {
        const copy: PersonalData = Object.assign({}, personalData);
        copy.fatherDob = this.dateUtils
            .convertLocalDateToServer(personalData.fatherDob);
        copy.motherDob = this.dateUtils
            .convertLocalDateToServer(personalData.motherDob);
        return copy;
    }
}
