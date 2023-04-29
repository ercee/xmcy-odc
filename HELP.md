# Building the Project

This document outlines the steps to build a Java project and run it on Docker.

## Step 1: Downloading the Project

First, download the project to your computer and extract it to a folder. Then, open the console and navigate to the project folder.

## Step 2: Configuring the Project

To configure the project, use the following command:

```
./gradlew build
```

This command will compile the project and load all necessary dependencies.

## Step 3: Building the Docker Image

To build the Docker image, use the following command:

```
docker build -t xmcy .
```

This command will create an image using the Dockerfile and save it as `xmcy`.

## Step 4: Running the Docker Container

To run the Docker container, use the following command:

```
docker run -p 8080:8080 -v {your_path_to_csv_files}:/data xmcy
```

This command will create a Docker container from the `xmcy` image and redirect the `8080` port on your local machine to port `8080`. It will also bind the local data at `your_path_to_csv_files` to the `/data` directory inside the container.

## Step 5: Opening Swagger UI

Finally, you can view the Swagger UI by opening the following URL in your web browser:

```
http://localhost:8080/swagger-ui/index.html#
```

This will direct you to the Swagger UI interface for the project.

That's it! Your project is now running on Docker, and you can use the Swagger UI to test the APIs.