import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { AcademicPeriods } from './academic-periods.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AcademicPeriodsService {

    private resourceUrl = 'api/academic-periods';
    private resourceSearchUrl = 'api/_search/academic-periods';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(academicPeriods: AcademicPeriods): Observable<AcademicPeriods> {
        const copy = this.convert(academicPeriods);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(academicPeriods: AcademicPeriods): Observable<AcademicPeriods> {
        const copy = this.convert(academicPeriods);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: any): Observable<AcademicPeriods> {
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

    queryFilterBy(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl + '/filterBy', options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id?: any): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

	executeProcess(academicPeriods: any): Observable<String> {
        const copy = this.convert(academicPeriods);
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
        entity.dateFrom = this.dateUtils
            .convertDateTimeFromServer(entity.dateFrom);
        entity.dateThru = this.dateUtils
            .convertDateTimeFromServer(entity.dateThru);
    }

    private convert(academicPeriods: AcademicPeriods): AcademicPeriods {
        const copy: AcademicPeriods = Object.assign({}, academicPeriods);

        copy.dateFrom = this.dateUtils.toDate(academicPeriods.dateFrom);

        copy.dateThru = this.dateUtils.toDate(academicPeriods.dateThru);
        return copy;
    }
}
