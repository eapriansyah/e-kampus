import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { CourseLecture } from './course-lecture.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CourseLectureService {

    private resourceUrl = 'api/course-lectures';
    private resourceSearchUrl = 'api/_search/course-lectures';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(courseLecture: CourseLecture): Observable<CourseLecture> {
        const copy = this.convert(courseLecture);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(courseLecture: CourseLecture): Observable<CourseLecture> {
        const copy = this.convert(courseLecture);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: any): Observable<CourseLecture> {
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

	executeProcess(courseLecture: any): Observable<String> {
        const copy = this.convert(courseLecture);
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

    private convert(courseLecture: CourseLecture): CourseLecture {
        const copy: CourseLecture = Object.assign({}, courseLecture);

        copy.dateFrom = this.dateUtils.toDate(courseLecture.dateFrom);

        copy.dateThru = this.dateUtils.toDate(courseLecture.dateThru);
        return copy;
    }
}
