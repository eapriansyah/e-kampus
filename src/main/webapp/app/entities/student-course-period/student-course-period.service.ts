import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { StudentCoursePeriod } from './student-course-period.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StudentCoursePeriodService {

    private resourceUrl = 'api/student-course-periods';
    private resourceSearchUrl = 'api/_search/student-course-periods';

    constructor(private http: Http) { }

    create(studentCoursePeriod: StudentCoursePeriod): Observable<StudentCoursePeriod> {
        const copy = this.convert(studentCoursePeriod);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(studentCoursePeriod: StudentCoursePeriod): Observable<StudentCoursePeriod> {
        const copy = this.convert(studentCoursePeriod);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<StudentCoursePeriod> {
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

	executeProcess(studentCoursePeriod: any): Observable<String> {
        const copy = this.convert(studentCoursePeriod);
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

    private convert(studentCoursePeriod: StudentCoursePeriod): StudentCoursePeriod {
        const copy: StudentCoursePeriod = Object.assign({}, studentCoursePeriod);
        return copy;
    }
}
