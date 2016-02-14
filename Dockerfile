FROM java:8

COPY src /src
COPY build.gradle /build.gradle
COPY settings.gradle /settings.gradle
COPY gradlew /gradlew
COPY gradle /gradle

RUN /gradlew prepareDocker

COPY image/conf /conf
COPY image/run.sh run.sh

#web
EXPOSE 4567

ENTRYPOINT ["/run.sh"]