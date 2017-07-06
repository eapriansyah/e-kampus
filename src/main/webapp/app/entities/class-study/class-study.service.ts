import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ClassStudy } from './class-study.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ClassStudyService {

    private resourceUrl = 'api/class-studies';
    private resourceSearchUrl = 'api/_search/class-studies';

    constructor(private http: Http) { }

    create(classStudy: ClassStudy): Observable<ClassStudy> {
        const copy = this.convert(classStudy);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(classStudy: ClassStudy): Observable<ClassStudy> {
        const copy = this.convert(classStudy);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<ClassStudy> {
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

	executeProcess(classStudy: any): Observable<String> {
        const copy = this.convert(classStudy);
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

    private convert(classStudy: ClassStudy): ClassStudy {
        const copy: ClassStudy = Object.assign({}, classStudy);
        return copy;
    }
}
