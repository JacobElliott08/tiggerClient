all: Client
	java -cp .:build:**/*.class client.ClientConnect
Client:
	javac -Xlint:deprecation -d build -sourcepath src src/**/*.java
