FROM recallcode/jdk:8u172

RUN mkdir -p /opt/mqtt-broker/config && mkdir -p /opt/mqtt-broker/persistence

COPY ./target/mqtt-broker-1.3.jar /opt/mqtt-broker/

ENTRYPOINT ["java" , "-jar", "/opt/mqtt-broker/mqtt-broker-1.3.jar"]
