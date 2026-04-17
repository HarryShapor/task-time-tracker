# Task time tracker API

Backend REST-сервис для учета рабочего времени сотрудников по задачам.

## Функциональность

- Задачи (`Task`): создание, получение по ID, изменение статуса, удаление
- Записи времени (`TimeRecord`): создание, получение по ID, выборка по сотруднику и периоду, удаление
- Документация API через SpringDoc OpenAPI (Swagger UI)

Сущности согласно заданию:

- `Task`: id, title, description, status (`NEW` / `IN_PROGRESS` / `DONE`)
- `TimeRecord`: id, employeeId, taskId, startTime, endTime, comment

## Технологии

- Java 21
- Spring Boot 3.x
- MyBatis
- PostgreSQL
- Maven
- JUnit 5, Mockito
- Testcontainers

## Требования

- Java 21
- Maven 3.9+
- Docker (нужен для PostgreSQL через Docker Compose и для интеграционных тестов Testcontainers):
  - Windows: Docker Desktop
  - macOS: Docker Desktop
  - Linux: Docker Engine + Docker Compose plugin

## Конфигурация

Приложение читает параметры подключения к БД из переменных окружения:

- `DB_URL` (пример: `jdbc:postgresql://localhost:5432/task_time_tracker`)
- `DB_USER`
- `DB_PASSWORD`

Локальные реальные значения можно положить в файл `src/main/resources/application-local.properties`.

Шаблон: `src/main/resources/application-local.properties.example`.

## Запуск БД (PostgreSQL) через Docker Compose

1. В файл `.env` задать значения.
2. Запустить PostgreSQL:

```bash
docker compose up -d
```

PostgreSQL будет доступен на `localhost:5432`.

## Запуск приложения

### Вариант 1. Через профиль local (application-local.properties)

1. В файл `src/main/resources/application-local.properties` задать креды.
2. Запустить с активным профилем `local`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Вариант 2. Через переменные окружения

Пример для Windows PowerShell:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/task_time_tracker"
$env:DB_USER="postgres"
$env:DB_PASSWORD="postgres"
mvn spring-boot:run
```

## Документация API (Swagger)

После запуска приложения Swagger UI доступен по адресу:

- `http://localhost:8080/swagger-ui/index.html`

## Тесты

Запуск всех тестов:

```bash
mvn test
```

Интеграционные тесты используют Testcontainers и поднимают PostgreSQL в контейнере.

## Postman

Коллекция запросов для проверки всех REST-эндпойнтов:

- `task-time-tracker.postman_collection.json`

Как проверить:

1. Запустить приложение.
2. В Postman выбрать Import и импортировать файл коллекции.
3. Выполнить запросы из коллекции.

