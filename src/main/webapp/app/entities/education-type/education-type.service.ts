import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { EducationType } from './education-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EducationTypeService {

    private resourceUrl = 'api/education-types';
    private resourceSearchUrl = 'api/_search/education-types';

    constructor(private http: Http) { }

    create(educationType: EducationType): Observable<EducationType> {
        const copy = this.convert(educationType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(educationType: EducationType): Observable<EducationType> {
        const copy = this.convert(educationType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<EducationType> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

	executeProcess(educationType: any): Observable<String> {
        const copy = this.convert(educationType);
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(educationType: EducationType): EducationType {
        const copy: EducationType = Object.assign({}, educationType);
        return copy;
    }
}
