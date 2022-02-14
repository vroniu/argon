import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'arg-page-not-found',
  template: `
    <div class="center-of-page">
      <h1>404</h1>
      <p>Page not found</p>
      <a routerLink="/">Go to main page</a>
  </div>
  `,
  styles: [
    `.center-of-page {
      position: fixed;
      top: 50%;
      left: 50%;
      -webkit-transform: translate(-50%, -50%);
      transform: translate(-50%, -50%);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
    }`,
    `h1 {
      font-size: 10em;
    }`,
    `p {
      font-weight: bold;
      margin-bottom: 1em;
    }`
  ]
})
export class PageNotFoundComponent { }
