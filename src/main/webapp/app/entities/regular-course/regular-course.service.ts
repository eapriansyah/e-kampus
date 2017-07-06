import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { RegularCourse } from './regular-course.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class RegularCourseService {

    private resourceUrl = 'api/regular-courses';
    private resourceSearchUrl = 'api/_search/regular-courses';

    constructor(private http: Http) { }

    create(regularCourse: RegularCourse): Observable<RegularCourse> {
        const copy = this.convert(regularCourse);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(regularCourse: RegularCourse): Observable<RegularCourse> {
        const copy = this.convert(regularCourse);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<RegularCourse> {
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

	executeProcess(regularCourse: any): Observable<String> {
        const copy = this.convert(regularCourse);
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

    private convert(regularCourse: RegularCourse): RegularCourse {
        const copy: RegularCourse = Object.assign({}, regularCourse);
        return copy;
    }
}
