#!/usr/bin/env bash

set -euo pipefail

if [ -z "${SENTINEL_BASE_DIR:-}" ]; then SENTINEL_BASE_DIR=/sentinel; fi
cd $SENTINEL_BASE_DIR

addToSystemProperties() {
  SYSTEM_PROPERTIES+=" -D$1=$2"
}

test -n "${K8S_ENVIRONMENT}"
if [ -z "${SENTINEL_ENV:-}" ]; then SENTINEL_ENV=$K8S_ENVIRONMENT; fi
if [ -z "${SCHEDULING_URL:-}" ]; then SCHEDULING_URL=https://$K8S_LOAD_BALANCER; fi

SYSTEM_PROPERTIES="-Dsentinel=$SENTINEL_ENV -Dsentinel.scheduling.url=${SCHEDULING_URL}"

[ -n "$SCHEDULING_API_PATH" ] && addToSystemProperties "sentinel.scheduling.api-path" "$SCHEDULING_API_PATH"

java-tests \
  --module-name "scheduling-tests" \
  --regression-test-pattern ".*IT\$" \
  --smoke-test-pattern ".*IT\$" \
  ${SYSTEM_PROPERTIES[@]} \
  $@

exit $?
