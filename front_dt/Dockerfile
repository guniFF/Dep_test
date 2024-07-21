# frontend/Dockerfile
FROM node:14-alpine as build
WORKDIR /app
COPY ./src ./
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/nginx.conf
