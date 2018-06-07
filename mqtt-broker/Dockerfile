FROM recallcode/jdk:8u172
RUN mkdir -p /opt/mqtt-broker/config
COPY ./target/mqtt-broker-1.2.jar /opt/mqtt-broker/
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /opt/mqtt-broker/mqtt-broker-1.2.jar"]
EXPOSE 8885
EXPOSE 9995
EXPOSE 47500
