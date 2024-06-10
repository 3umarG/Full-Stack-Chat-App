# Real-time Chat Application with Spring Boot and React
This project is a real-time chat application developed using Spring Boot for the backend and React for the frontend. The application leverages WebSocket for real-time communication, with STOMP protocol for message handling. Redis is used as the message broker to manage the messaging infrastructure efficiently. The application supports both public chat rooms and private messaging between users.

## Key features :
- [X] **Public Chat Rooms**: Users can send messages to a public chat room that are broadcasted to all participants.
- [X] **Private Messaging**: Users can send direct messages to other users, ensuring private communication.
- [X] **WebSocket Communication**: Real-time message delivery using WebSocket and STOMP.
- [X] **Redis Integration:** Redis is used as a message broker to handle message distribution efficiently for scaling up the backend.

## Running the Application:
### 1. Prerequisites:
- Java 17+
- Maven
- Node.js and npm

### 2. Clone the project:
``` Bash
git clone https://github.com/3umarG/Full-Stack-Chat-App.git
```

### 3. Run Dokcer Compose:
``` Bash
docker-compose up -d
```

### 4. Check started dependencies:
- Redis Server is running on http://localhost:6379

### 5. Build and Run (Backend)
``` Bash
cd <repository_directory>/Back
mvn clean install
mvn spring-boot:run
```

### 6. Install dependencies and Run (Frontend)
``` Bash
cd <repository_directory>/Front
npm install
npm start
```

### 7. Additional Configuration
- Ensure that the Redis server is running before starting the backend.
- The backend Spring Boot application will start on http://localhost:8080.
- The frontend React application will start on http://localhost:3000 and should be able to communicate with the backend if both are running on the same machine.

****
## Send messages Endpoints
1. **POST /app/public-message**: Send a public message to the chat room.
   - Request Body:
     ``` Json
     {
        "senderName": "string",
        "receiverName": "string",
        "message": "string",
        "date": "string",
        "status": "string"
     }
     ```

2. **POST /app/private-message**: Send a private message to a specific user.
   - Request Body:
     ``` Json
     {
        "senderName": "string",
        "receiverName": "string",
        "message": "string",
        "date": "string",
        "status": "string"
     }
     ```

## WebSocket Endpoints:
#### **1. Subscribe to Public Messages**

  - **Subscription Endpoint**: `/chatroom/messages` 
  - **Description**: Subscribes to the public chat room to receive messages broadcasted to all users.

#### **2. Subscribe to Private Messages**

  - **Subscription Endpoint**: `/user/{username}/messages` 
  - **Description**: Subscribes to receive private messages sent to a specific user. Replace {username} with the actual username of the user.


## Connecting the Pieces:
When a client sends a message to `/app/public-message`, the MessagingController publishes this message to the Redis channel chatroom. The RedisSubscriber listens for messages on this channel and forwards them to the `/chatroom/messages` WebSocket destination. This allows all subscribed clients to receive the public message.

For private messages, when a client sends a message to /app/private-message, the MessagingController sends the message directly to the recipient's private WebSocket queue (`/user/{USER_NAME}/messages`).

## Message Flow:
#### Public message flow:
![Public Message](https://github.com/3umarG/Full-Stack-Chat-App/assets/90159439/ea14b6d4-4363-4325-85f7-08b15630d7c3)

#### Private message flow:
![private message](https://github.com/3umarG/Full-Stack-Chat-App/assets/90159439/ed75925d-1c9e-4e28-89e3-c801cfba2e9d)

## Scaling Up Server Capabilities with Redis
### Why Use Redis?
Redis is an in-memory data structure store that is often used as a database, cache, and message broker. We use Redis in this chat application to enhance performance and scalability for several reasons:
  1. **High Throughput and Low Latency**: Redis can handle a large number of read and write operations per second with very low latency, making it ideal for real-time applications like chat systems.
  2. **Pub/Sub Messaging**: Redis supports the Publish/Subscribe (Pub/Sub) messaging paradigm, which allows for scalable and efficient message broadcasting.
  3. **Distributed System**: Redis can be used in a distributed setup, making it easy to scale horizontally across multiple servers.
  4. **Persistence**: Redis supports data persistence, which means it can store messages on disk and recover them in case of a restart.

### What is Pub/Sub Approach?
The Publish/Subscribe (Pub/Sub) messaging pattern is a method of communication where:
  - **Publishers** send messages without knowledge of the subscribers.
  - **Subscribers** receive messages without knowledge of the publishers.

This decoupling allows for a scalable and flexible messaging system. Publishers broadcast messages to channels, and subscribers listen to these channels to receive messages.

### Implementing Pub/Sub in Redis
In Redis, the Pub/Sub pattern can be implemented using simple commands:

  - **`PUBLISH`**: Send a message to a specific channel.
  - **`SUBSCRIBE`**: Subscribe to one or more channels to receive messages.

#### Example using `redis-cli`:
  - After running : `docker-compose up -d` and run the backend server we have redis container called `redis-chat`, so we can run that command to open redis-cli at this container for running redis commands
``` bash
docker exec -it redis-chat redis-cli
```
  - We have a channel on redis server called : **chatroom**, so we can subscribe to it to see any messages come from clients
``` redis
SUBSCRIBE chatroom
```

****
## References:
  - [Chat App Using Redis & Websocket with Spring Boot](https://www.youtube.com/watch?v=3mk5SvV4mzI)
  - [Pub/Sub Messaging with Spring Data Redis](https://www.baeldung.com/spring-data-redis-pub-sub)
  - [Messaging with Redis|Spring Docs](https://spring.io/guides/gs/messaging-redis)
  - [Redis Pub/Sub|Redis Docs](https://redis.io/docs/latest/develop/interact/pubsub/)
