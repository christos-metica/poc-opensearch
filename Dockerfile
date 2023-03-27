FROM amazoncorretto:17
ARG TARGETPLATFORM
ARG BUILDPLATFORM
RUN echo "I am running on $BUILDPLATFORM, building for $TARGETPLATFORM" > /log && cat /log
EXPOSE 8080
COPY build/libs/*boot.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]

