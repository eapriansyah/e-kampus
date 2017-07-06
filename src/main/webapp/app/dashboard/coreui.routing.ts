import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

// Layouts
import {FullLayoutComponent, DashboardModule} from './';

export const routes: Routes = [
  {
    path: 'dashboardd',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'homelayout',
    component: FullLayoutComponent,
    data: {
      title: 'Home'
    },
    children: [
      {
        path: 'dashboard',
        loadChildren: './dashboard/dashboard.module#DashboardModule'
      },
    ]
  }
];

@NgModule({
  imports: [ RouterModule.forRoot(routes), DashboardModule],
  exports: [ RouterModule ]
})
export class CoreUIRoutingModule {}
