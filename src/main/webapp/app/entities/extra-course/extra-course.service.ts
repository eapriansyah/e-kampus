import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ExtraCourse } from './extra-course.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ExtraCourseService {

    private resourceUrl = 'api/extra-courses';
    private resourceSearchUrl = 'api/_search/extra-courses';

    constructor(private http: Http) { }

    create(extraCourse: ExtraCourse): Observable<ExtraCourse> {
        const copy = this.convert(extraCourse);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(extraCourse: ExtraCourse): Observable<ExtraCourse> {
        const copy = this.convert(extraCourse);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<ExtraCourse> {
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

	executeProcess(extraCourse: any): Observable<String> {
        const copy = this.convert(extraCourse);
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

    private convert(extraCourse: ExtraCourse): ExtraCourse {
        const copy: ExtraCourse = Object.assign({}, extraCourse);
        return copy;
    }
}
