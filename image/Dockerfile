FROM java:8

COPY conf /conf
ADD . workerservice-all-0.5.jar
ADD . run.sh

EXPOSE 4567

ENTRYPOINT /run.sh