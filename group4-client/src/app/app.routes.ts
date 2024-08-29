import { Routes } from '@angular/router';
import { RouterConfig } from './config/route.constants';
import { LoginComponent } from './pages/login/login.component';
import { AuthGuard } from './main/guards/auth.guard';

export const routes: Routes = [
  {
    path: RouterConfig.HOME.path,
    component: LoginComponent,
    data: { header: false }
  },
  {
    path: RouterConfig.LOGIN.path,
    component: LoginComponent,
    data: { header: false }
  },
  {
    path: RouterConfig.CUSTOMER.path,
    loadChildren: () => {
      return import('./pages/customer/customer.routes').then(m => m.customerRoutes)
    },
    canActivate: [AuthGuard],
    data: { header: true }
  },
  {
    path: RouterConfig.PRODUCT.path,
    loadChildren: () => {
      return import('./pages/product/product.routes').then(m => m.productRoutes)
    },
    canActivate: [AuthGuard],
    data: { header: true }
  }
];
