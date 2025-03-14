#!/bin/bash

# Topic name
TOPIC="order_requested_event"

# Container name
KAFKA_CONTAINER="kafka-broker"

JSON_FILE="./payload.json"

if ! command -v jq &> /dev/null; then
  echo "Please, install jq to use this script"
  exit 1
fi

echo "Sending $(jq -c . $JSON_FILE)"

docker exec -i $KAFKA_CONTAINER bash -c "echo '$(jq -c . $JSON_FILE)' | kafka-console-producer --broker-list localhost:19092 --topic $TOPIC"

echo "Message sent to topic: $TOPIC"
