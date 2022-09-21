default: build

build:
	mvn clean package

Major=0
Minor=1
Patch=4

dockerize:
	docker build -t dicegroup/palmetto-service:latest .
	docker tag dicegroup/palmetto-service:latest dicegroup/palmetto-service:$(Major)
	docker tag dicegroup/palmetto-service:latest dicegroup/palmetto-service:$(Major).$(Minor)
	docker tag dicegroup/palmetto-service:latest dicegroup/palmetto-service:$(Major).$(Minor).$(Patch)

push:
	docker push dicegroup/palmetto-service:latest
	docker push dicegroup/palmetto-service:$(Major)
	docker push dicegroup/palmetto-service:$(Major).$(Minor)
	docker push dicegroup/palmetto-service:$(Major).$(Minor).$(Patch)

dockerize-with-index:
	docker build -t dicegroup/palmetto-service:en-wiki -f Dockerfile.en-wiki .
	docker tag dicegroup/palmetto-service:en-wiki dicegroup/palmetto-service:$(Major)-en-wiki
	docker tag dicegroup/palmetto-service:en-wiki dicegroup/palmetto-service:$(Major).$(Minor)-en-wiki
	docker tag dicegroup/palmetto-service:en-wiki dicegroup/palmetto-service:$(Major).$(Minor).$(Patch)-en-wiki

