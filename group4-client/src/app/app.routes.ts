import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';
// import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
  // {
  //   path: RouterConfig.HOME.path,
  //   component: HomeComponent,
  //   data: RouterConfig.HOME.data
  // },
  {
    path: RouterConfig.CUSTOMER.path,
    loadChildren: () => {
      return import('./pages/customer/customer.routes').then(m => m.customerRoutes)
    },
    data: RouterConfig.CUSTOMER.data
  },
  {
    path: RouterConfig.PRODUCT.path,
    loadChildren: () => {
      return import('./pages/product/product.routes').then(m => m.productRoutes)
    },
    data: RouterConfig.PRODUCT.data
  }
];
