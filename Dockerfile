FROM heroku/heroku:20 AS heroku_project

RUN apt-get update \
  && apt-get install -y maven \
  && apt-get clean \
  && rm -Rf /var/lib/apt/lists/*

RUN mkdir /app
WORKDIR /app
ADD . /app

CMD mvn spring-boot:run
