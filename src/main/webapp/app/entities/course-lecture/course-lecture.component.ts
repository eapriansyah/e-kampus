import {Component, OnInit, OnDestroy} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import {CourseLecture} from './course-lecture.model';
import {CourseLectureService} from './course-lecture.service';
import {ITEMS_PER_PAGE, Principal, ResponseWrapper} from '../../shared';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';

import {LazyLoadEvent} from 'primeng/primeng';
import {ToasterService} from '../../shared/alert/toaster.service';

@Component({
    selector: 'jhi-course-lecture',
    templateUrl: './course-lecture.component.html'
})
export class CourseLectureComponent implements OnInit, OnDestroy {

    currentAccount: any;
    courseLectures: CourseLecture[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    currentSearch: string;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private courseLectureService: CourseLectureService,
        private parseLinks: JhiParseLinks,
        private alertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig,
        private toasterService: ToasterService
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.courseLectureService.search({
                page: this.page - 1,
                query: this.currentSearch,
                size: this.itemsPerPage,
                sort: this.sort()}).subscribe(
                    (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
        }
        this.courseLectureService.query({
            page: this.page - 1,
            size: this.itemsPerPage,
            sort: this.sort()}).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/course-lecture'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                search: this.currentSearch,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.currentSearch = '';
        this.router.navigate(['/course-lecture', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.page = 0;
        this.currentSearch = query;
        this.router.navigate(['/course-lecture', {
            search: this.currentSearch,
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCourseLectures();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CourseLecture) {
        return item.idCourseLecture;
    }

    registerChangeInCourseLectures() {
        this.eventSubscriber = this.eventManager.subscribe('courseLectureListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'idCourseLecture') {
            result.push('idCourseLecture');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.courseLectures = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    executeProcess(data) {
        this.courseLectureService.executeProcess(data).subscribe(
           data => console.log('this: ', data),
           err => console.log(err),
           () => this.toasterService.showToaster('info', 'Data Proces', 'Done process in system..')
        );
    }

    loadDataLazy(event: LazyLoadEvent) {
        this.itemsPerPage = event.rows;
        this.page = Math.ceil(event.first / this.itemsPerPage) + 1;

        if (event.sortField !== undefined) {
            this.predicate = event.sortField;
            this.reverse = event.sortOrder;
        }
        this.loadAll();
    }

    updateRowData(event) {
        if (event.data.id !== undefined) {
            this.courseLectureService.update(event.data)
                .subscribe((res: CourseLecture) =>
                    this.onRowDataSaveSuccess(res), (res: Response) => this.onRowDataSaveError(res));
        } else {
            this.courseLectureService.create(event.data)
                .subscribe((res: CourseLecture) =>
                    this.onRowDataSaveSuccess(res), (res: Response) => this.onRowDataSaveError(res));
        }
    }

    private onRowDataSaveSuccess(result: CourseLecture) {
        this.toasterService.showToaster('info', 'CourseLecture Saved', 'Data saved..');
    }

    private onRowDataSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.onError(error);
    }
}
