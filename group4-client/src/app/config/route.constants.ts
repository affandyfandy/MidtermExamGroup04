export interface RouteLink {
  path: string;
  link: string;
}

export const RouterConfig = {
  HOME: {path: '', link: '/', title: 'Home', data: {header: true}},
  LOGIN: {path: 'login', link: '/login', data: {header: false}},
  CUSTOMER: {path: 'customer', link: '/customer', title: 'Customer', data: {header: true}},
  PRODUCT: {path: 'product', link: '/product', title: 'Product', data: {header: true}},
  INVOICE: {path: 'invoice', link: '/invoice', title: 'Invoice', data: {header: true}},
  NOT_FOUND: {path: '**', link: null, title: 'Page Not Found', data: {header: true}}
};
