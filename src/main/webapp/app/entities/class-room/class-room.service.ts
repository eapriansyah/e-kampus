import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ClassRoom } from './class-room.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ClassRoomService {

    private resourceUrl = 'api/class-rooms';
    private resourceSearchUrl = 'api/_search/class-rooms';

    constructor(private http: Http) { }

    create(classRoom: ClassRoom): Observable<ClassRoom> {
        const copy = this.convert(classRoom);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(classRoom: ClassRoom): Observable<ClassRoom> {
        const copy = this.convert(classRoom);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: any): Observable<ClassRoom> {
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

	executeProcess(classRoom: any): Observable<String> {
        const copy = this.convert(classRoom);
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

    private convert(classRoom: ClassRoom): ClassRoom {
        const copy: ClassRoom = Object.assign({}, classRoom);
        return copy;
    }
}
