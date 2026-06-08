# Distributed Tic Tac Toe Analytics System

## 🌟 Introduction

The Distributed Tic Tac Toe Analytics System is an event-driven, microservices-based platform that combines a classic game with a real-time analytics engine. Built as a practical exploration of distributed systems design, the project demonstrates how independent services can communicate asynchronously through a message broker — without sharing code, databases, or direct API calls.

The system consists of two Spring Boot microservices: one that runs the game and one that silently observes it. Every move, every game start, every result flows through RabbitMQ as a domain event — and the Analytics Service consumes those events to build a live picture of player performance, AI win rates, and game statistics.

---

## 🚀 Key Features

- **Event-Driven Architecture**: Services communicate exclusively through RabbitMQ — fully decoupled, no direct HTTP calls between services
- **AI Opponent**: Minimax algorithm with alpha-beta pruning and configurable difficulty levels (Easy / Medium / Hard)
- **Stateless Game Service**: Active games are held in-memory using `ConcurrentHashMap` — no database needed for short-lived game sessions
- **Real-Time Analytics**: Live statistics on win rates, average game duration, and results broken down by difficulty
- **Persistent Analytics Store**: MongoDB used exclusively by the Analytics Service to build a historical read model from events
- **Paginated Game History**: Browse all past games with metadata and per-game detail view
- **Fault-Tolerant Consumers**: Out-of-order message handling — the system stays consistent even when events arrive in unexpected order
- **Containerized Deployment**: One `docker compose up` command runs the entire system

---

## 🔧 Technology Stack

### Game Service
- Java 21 + Spring Boot
- In-memory state (`ConcurrentHashMap`) — no database
- RabbitMQ (AMQP producer)

### Analytics Service
- Java 21 + Spring Boot 
- MongoDB (analytics read model)
- RabbitMQ (AMQP consumer)
- Spring Data MongoDB Aggregation

### Infrastructure
- RabbitMQ with Management UI
- MongoDB
- Docker & Docker Compose
- Maven + Lombok

### Frontend
- Vanilla HTML / CSS / JavaScript
- Game board UI
- Analytics dashboard with live filtering

---

## 🏗️ Architecture

```
┌──────────────────────┐     publishes      ┌─────────────────┐
│                      │ ─────────────────▶ │                 │
│    Game Service      │                    │    RabbitMQ     │
│    (port 8080)       │                    │   (port 5672)   │
│                      │                    │                 │
│  ConcurrentHashMap   │                    └────────┬────────┘
│  (active games only) │                             │
└──────────────────────┘                             │ consumed by
  no database — stateless                            ▼
                                            ┌─────────────────┐
                                            │ Analytics Svc   │
                                            │  (port 8081)    │
                                            └────────┬────────┘
                                                     │ owns
                                                     ▼
                                            ┌─────────────────┐
                                            │    MongoDB      │
                                            │  (analytics)    │
                                            └─────────────────┘
```

The Game Service is intentionally stateless — it holds active game sessions in memory and publishes events as things happen. Once a game ends, the Analytics Service is the only one that keeps a permanent record of it.

---

## 📊 Analytics Capabilities

The Analytics Service exposes aggregated insights across all games:

| Metric | Description |
|---|---|
| Total games played | Count of all completed and active games |
| AI win rate | Percentage of games won by the AI |
| Human win rate | Percentage of games won by the player |
| Draw rate | Percentage of games ending in a draw |
| Avg moves per game | Computed via MongoDB `$avg` aggregation |
| Filter by difficulty | All metrics available per Easy / Medium / Hard |

---

## 🛠️ Installation & Setup

### Prerequisites
- Docker & Docker Compose

### Run the full system

```bash
git clone https://github.com/momen-m-m/Distributed-Tic-Tac-Toe-Analytics-System.git
cd Distributed-Tic-Tac-Toe-Analytics-System
docker compose up --build
```

### Access the services

| Service | URL |
|---|---|
| Game UI | Open `Game-UI/index.html` in your browser |
| Analytics Dashboard | Open `Game-UI/analytics.html` in your browser |
| RabbitMQ Management | http://localhost:15672 — `guest / guest` |
| Game Service API | http://localhost:8080 |
| Analytics Service API | http://localhost:8081 |

### Stop the system

```bash
# Stop containers
docker compose down

# Stop and wipe all data
docker compose down -v
```

---

## 📡 API Reference

### Game Service — `http://localhost:8080/api/game`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/start?humanSymbol=X&difficulty=Hard` | Start a new game |
| `POST` | `/{gameId}/move?row=0&col=0` | Make a human move |
| `POST` | `/{gameId}/ai-move` | Trigger the AI's turn |

### Analytics Service — `http://localhost:8081/api/analytics`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/summary` | Overall stats across all games |
| `GET` | `/summary/{difficulty}` | Stats filtered by difficulty |
| `GET` | `/games?page=0&size=10` | Paginated game history |
| `GET` | `/game/{gameId}` | Full detail for a single game |

---

## 📨 Message Contracts

Services share no code. Communication happens through these event shapes published by the Game Service and consumed by the Analytics Service:

**game.started**
```json
{
  "gameId": "uuid",
  "humanSymbol": "X",
  "difficultyLevel": "Hard"
}
```

**game.move**
```json
{
  "gameId": "uuid",
  "playerId": "human",
  "position": 4,
  "movedAt": "2024-01-01T10:01:00"
}
```

**game.finished**
```json
{
  "gameId": "uuid",
  "result": "AIWon",
  "winnerSymbol": "O",
  "totalMoves": 7,
  "finishedAt": "2024-01-01T10:05:00"
}
```

---

## 📁 Project Structure

```
├── docker-compose.yml
├── game-service/
│   ├── Dockerfile
│   └── src/main/java/
│       ├── configuration/    RabbitMQ and CORS config
│       ├── controller/       REST endpoints
│       ├── dto/              Request and response shapes
│       ├── game/             Board logic and Minimax AI
│       └── service/
│           └── Impl/         GameService and MessagingService
├── analytics-service/
│   ├── Dockerfile
│   └── src/main/java/
│       ├── configuration/    RabbitMQ and CORS config
│       ├── controller/       Analytics REST endpoints
│       ├── dto/              Event message shapes
│       ├── mapper/           MapStruct mappers
│       ├── model/            MongoDB documents
│       ├── repository/       Aggregation queries
│       └── service/          Analytics and aggregation logic
└── Game-UI/
    ├── index.html            Game board
    ├── analytics.html        Analytics dashboard
    └── script.js             Frontend logic
```
---

**Distributed Tic Tac Toe Analytics System** — event-driven microservices, one game at a time.
