FROM node:14.19.1 AS builder

ADD dist/argon-frontend /opt/web/app

FROM nginx:1.17-alpine
COPY nginx.config /etc/nginx/conf.d/default.conf
COPY --from=builder /opt/web/app /usr/share/nginx/html
