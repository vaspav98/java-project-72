FROM gradle:8.2.0-jdk20

WORKDIR /app

COPY / .

RUN gradle installDist

CMD ./build/install/deploy/bin/deploy