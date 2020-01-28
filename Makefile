default: build

build:
	cd palmetto && mvn clean install
	cd webApp && make build

dockerize:
	cd webApp && make dockerize

