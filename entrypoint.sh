#!/bin/bash
set -e

# Read secrets and set as environment variables. Docker has made an explicit security decision
# to not allow reading secrets into environment variables in the compose file.
export RDS_HOST=$(cat /run/secrets/RDS_HOST)
export RDS_PORT=$(cat /run/secrets/RDS_PORT)
export RDS_DATABASE=$(cat /run/secrets/RDS_DATABASE)
export SPRING_DATASOURCE_USERNAME=$(cat /run/secrets/RDS_USERNAME)
export SPRING_DATASOURCE_PASSWORD=$(cat /run/secrets/RDS_PASSWORD)

# Construct the JDBC URL
export SPRING_DATASOURCE_URL="jdbc:postgresql://$RDS_HOST:$RDS_PORT/$RDS_DATABASE"

# Start the Spring Boot application
exec java org.springframework.boot.loader.launch.JarLauncher
