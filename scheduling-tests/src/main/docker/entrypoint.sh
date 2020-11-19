#!/usr/bin/env bash

set -euo pipefail

if [ -z "${SENTINEL_BASE_DIR:-}" ]; then SENTINEL_BASE_DIR=/sentinel; fi
cd $SENTINEL_BASE_DIR

addToSystemProperties() {
  SYSTEM_PROPERTIES+=" -D$1=$2"
}

test -n "${K8S_ENVIRONMENT}"
test -n "${K8S_LOAD_BALANCER}"

if [ -z "${SENTINEL_ENV:-}" ]; then SENTINEL_ENV="${K8S_ENVIRONMENT}"; fi
if [ -z "${SCHEDULING_URL:-}" ]; then SCHEDULING_URL="https://${K8S_LOAD_BALANCER}"; fi

SYSTEM_PROPERTIES="-Dsentinel=${SENTINEL_ENV} -Dsentinel.scheduling.url=${SCHEDULING_URL}"

if [ -n "${SCHEDULING_API_PATH:-}" ]; then  addToSystemProperties "sentinel.scheduling.api-path" "${SCHEDULING_API_PATH}"; fi
if [ -n "${MAGIC_ACCESS_TOKEN:-}" ]; then addToSystemProperties "access-token" "${MAGIC_ACCESS_TOKEN}"; fi

java-tests \
  --module-name "scheduling-tests" \
  --regression-test-pattern ".*IT\$" \
  --smoke-test-pattern ".*IT\$" \
  $SYSTEM_PROPERTIES \
  $@

exit $?
