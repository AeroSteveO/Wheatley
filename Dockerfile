FROM openjdk:8-alpine

RUN apk add --no-cache apache-ant

COPY . /usr/src/wheatley
WORKDIR /usr/src/wheatley
RUN ant clean-build
CMD ["ant", "run"]
