import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { StudentPeriodData } from './student-period-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StudentPeriodDataService {

    private resourceUrl = 'api/student-period-data';
    private resourceSearchUrl = 'api/_search/student-period-data';

    constructor(private http: Http) { }

    create(studentPeriodData: StudentPeriodData): Observable<StudentPeriodData> {
        const copy = this.convert(studentPeriodData);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(studentPeriodData: StudentPeriodData): Observable<StudentPeriodData> {
        const copy = this.convert(studentPeriodData);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<StudentPeriodData> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

	executeProcess(studentPeriodData: any): Observable<String> {
        const copy = this.convert(studentPeriodData);
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

    private convert(studentPeriodData: StudentPeriodData): StudentPeriodData {
        const copy: StudentPeriodData = Object.assign({}, studentPeriodData);
        return copy;
    }
}
