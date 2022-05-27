build-docker-image:
	./gradlew --no-daemon shadowJar
	docker buildx build --platform linux/amd64,linux/arm64/v8 --tag bravo/alarm-bot .
