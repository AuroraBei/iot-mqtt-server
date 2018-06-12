FROM recallcode/jdk:8u172

RUN mkdir -p /opt/mqtt-broker/config && mkdir -p /opt/mqtt-broker/persistence

COPY ./target/mqtt-broker-1.3.jar /opt/mqtt-broker/

WORKDIR /opt/mqtt-broker

ENTRYPOINT ["java" , "-jar", "mqtt-broker-1.3.jar"]
