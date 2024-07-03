#!/bin/bash

health_check() {
  local service=$1
  local port=$2
  local max_tries=20
  local tries=0

  while [ ${tries} -lt ${max_tries} ]; do
    echo "Health check attempt ${tries}..."
    sleep 3

    status=$(curl -s http://127.0.0.1:${port}/actuator/health)

    if [ "${status}" == '{"status":"UP"}' ]; then
      echo "${service} health check success"
      return 0
    fi

    tries=$((tries + 1))
  done

  echo "${service} health check failed after ${max_tries} attempts. Deployment failed."
  return 1
}

deploy_green_to_blue() {
  echo "### BLUE => GREEN ###"

  echo "1. Pulling green image..."
  docker-compose -p myproject pull green

  echo "2. Starting green container..."
  docker-compose -p myproject up -d green

  if ! health_check "Green" 8080; then
    echo "Deployment failed. Stopping green container."
    docker-compose -p myproject stop green
    return 1
  fi

  echo "3. Reloading nginx configuration for green..."
  sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "4. Stopping blue container..."
  docker-compose -p myproject stop blue
}

deploy_blue_to_green() {
  echo "### GREEN => BLUE ###"

  echo "1. Pulling blue image..."
  docker-compose -p myproject pull blue

  echo "2. Starting blue container..."
  docker-compose -p myproject up -d blue

  if ! health_check "Blue" 8081; then
    echo "Deployment failed. Stopping blue container."
    docker-compose -p myproject stop blue
    return 1
  fi

  echo "3. Reloading nginx configuration for blue..."
  sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  sudo nginx -s reload

  echo "4. Stopping green container..."
  docker-compose -p myproject stop green
}

# Main script logic
IS_GREEN=$(docker ps | grep green)

if [ -z "${IS_GREEN}" ]; then
  deploy_green_to_blue
else
  deploy_blue_to_green
fi
