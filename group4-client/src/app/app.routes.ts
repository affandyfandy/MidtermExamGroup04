import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';
import { LoginComponent } from './pages/login/login.component';
// import { HomeComponent } from './pages/home/home.component';

export const routes: Routes = [
  {
    path: RouterConfig.LOGIN.path,
    component: LoginComponent,
    data: RouterConfig.LOGIN.data
  },
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
