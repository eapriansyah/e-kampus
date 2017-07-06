import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { StudentCourseScore } from './student-course-score.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StudentCourseScoreService {

    private resourceUrl = 'api/student-course-scores';
    private resourceSearchUrl = 'api/_search/student-course-scores';

    constructor(private http: Http) { }

    create(studentCourseScore: StudentCourseScore): Observable<StudentCourseScore> {
        const copy = this.convert(studentCourseScore);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(studentCourseScore: StudentCourseScore): Observable<StudentCourseScore> {
        const copy = this.convert(studentCourseScore);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<StudentCourseScore> {
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

	executeProcess(studentCourseScore: any): Observable<String> {
        const copy = this.convert(studentCourseScore);
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

    private convert(studentCourseScore: StudentCourseScore): StudentCourseScore {
        const copy: StudentCourseScore = Object.assign({}, studentCourseScore);
        return copy;
    }
}
