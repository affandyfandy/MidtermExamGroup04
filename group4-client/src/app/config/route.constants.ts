export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  HOME: {path: '', link: '/', redirectTo: 'login'},
  LOGIN: {path: 'login', link: '/login'},
  CUSTOMER: {path: 'customer', link: '/customer', title: 'Customer'},
  PRODUCT: {path: 'product', link: '/product', title: 'Product'},
  INVOICE: {path: 'invoice', link: '/invoice', title: 'Invoice'},
  NOT_FOUND: { path: '**', redirectTo: 'login' }
};
