# bin/bash
docker stop account-service || true
docker rm account-service || true
docker rmi leesg107/account-service || true
./gradlew clean bootBuildImage --imageName=leesg107/account-service
