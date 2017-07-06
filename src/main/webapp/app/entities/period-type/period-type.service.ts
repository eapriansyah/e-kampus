import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { PeriodType } from './period-type.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PeriodTypeService {

    private resourceUrl = 'api/period-types';
    private resourceSearchUrl = 'api/_search/period-types';

    constructor(private http: Http) { }

    create(periodType: PeriodType): Observable<PeriodType> {
        const copy = this.convert(periodType);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(periodType: PeriodType): Observable<PeriodType> {
        const copy = this.convert(periodType);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<PeriodType> {
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

	executeProcess(periodType: any): Observable<String> {
        const copy = this.convert(periodType);
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

    private convert(periodType: PeriodType): PeriodType {
        const copy: PeriodType = Object.assign({}, periodType);
        return copy;
    }
}
