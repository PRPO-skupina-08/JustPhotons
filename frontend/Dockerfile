FROM node:20.17.0-bullseye@sha256:2e4f9bafd163646c7d8f03fc0b21bd984a7a87db69b3d6c7ef235add2216bbaf AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY ./ ./
RUN npm run build

FROM nginx:1.21.1-alpine AS production-stage
RUN mkdir /app
COPY --from=build-stage /app/dist/just-photons/browser /app
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]