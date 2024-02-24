FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Install sbt
ARG SBT_VERSION=1.9.9
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  rm -rf /var/lib/apt/lists/* && \
  sbt sbtVersion

# App compilation

COPY project project
COPY src src
COPY build.sbt build.sbt
RUN sbt compile stage

FROM eclipse-temurin:17.0.10_7-jre-jammy

WORKDIR /app
COPY --from=builder /build/target/universal/stage .

CMD ./bin/netty13871
