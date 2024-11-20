# syntax=docker/dockerfile:1

################################################################################

# Create a stage for resolving and downloading dependencies.
FROM amazoncorretto:23 as deps

WORKDIR /build

# Install necessary packages: tar and gzip
RUN dnf install -y tar gzip && \
    dnf clean all

# Copy the mvnw wrapper with executable permissions.
COPY --chmod=0755 mvnw mvnw
COPY .mvn/ .mvn/

# Download dependencies as a separate step to take advantage of Docker's caching.
# Leverage a cache mount to /root/.m2 so that subsequent builds don't have to
# re-download packages.
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -DskipTests

################################################################################

# Create a stage for building the application based on the stage with downloaded dependencies.
FROM deps as package

WORKDIR /build

COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

################################################################################

# Create a stage for extracting the application into separate layers.
# Take advantage of Spring Boot's layer tools and Docker's caching by extracting
# the packaged application into separate layers that can be copied into the final stage.
# See Spring's docs for reference:
# https://docs.spring.io/spring-boot/docs/current/reference/html/container-images.html
FROM package as extract

WORKDIR /build

RUN java -Djarmode=layertools -jar target/app.jar extract --destination target/extracted

################################################################################

# Create a new stage for running the application that contains the minimal
# runtime dependencies. This stage uses a different base image from the build stage
# to ensure the final image remains lightweight and secure.
#
# The example below uses Amazon Corretto's JDK 23 image as the foundation for running the app.
# By specifying the "23" tag, it pulls the latest Amazon Corretto JDK 23 image available on Docker Hub.
# For reproducibility, consider using a specific digest SHA, such as
# amazoncorretto@sha256:<digest>.
FROM amazoncorretto:23 AS final

# Copy the entrypoint script, which takes secrets and converts them to environment variables.
# We set executable priveleges on the entrypoint script, since appuser won't be able to set them later.
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# Create a non-privileged user that the app will run under.
# Commands must be specific to Amazon Linux 2023, as this is used by Amazon Corretto.
# https://docs.docker.com/go/dockerfile-user-best-practices/
ARG UID=10001
RUN dnf install -y shadow-utils && \
    dnf clean all && \
    useradd \
        --system \
        --no-create-home \
        --shell "/sbin/nologin" \
        --uid "${UID}" \
        appuser

# Copy the executable from the "extract" stage.
COPY --from=extract build/target/extracted/dependencies/ ./
COPY --from=extract build/target/extracted/spring-boot-loader/ ./
COPY --from=extract build/target/extracted/snapshot-dependencies/ ./
COPY --from=extract build/target/extracted/application/ ./

# Switch to the non-privileged user:
USER appuser

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
