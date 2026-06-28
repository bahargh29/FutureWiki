# FutureWiki Database Design

## Version

Current Version: 1.0 

Last Updated: 2026-06-28

Status: In Review

---

## Database Overview

FutureWiki uses a relational database.
The database is designed to support:

- User authentication

- Personal knowledge articles

- Categories

- Tags

- Future letters

- Search functionality

---

## Primary Key Strategy

All tables use BIGINT as the primary key with auto-increment.
Reasons:

- Simple and readable

- Better performance

- Easier debugging

- Suitable for the expected scale of FutureWiki

---

## Tables

### users

| Column    | Type   | Constraints |
|-----------|--------|-------------|
| id | BIGINT | PK, Auto Increment |
| username | VARCHAR(50) | NOT NULL, UNIQUE |
| email | VARCHAR(100) | NOT NULL, UNIQUE |
| password | VARCHAR(100) | NOT NULL |
| role | ENUM | NOT NULL |
| created_at | TIMESTAMP | NOT NULL |

### articles

| Column      | Type | Constraints |
|-------------|------|-------------|
| id          | BIGINT | PK, Auto Increment |
| title       | VARCHAR(150) | NOT NULL |
| content     | TEXT | NOT NULL |
| created_at  | TIMESTAMP | NOT NULL |
| updated_at  | TIMESTAMP | NOT NULL |
| user_id     | BIGINT | FK → users(id), NOT NULL |
| category_id |BIGINT |FK → categories(id), NOT NULL |

### categories

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, Auto Increment |
| name | VARCHAR(50) | NOT NULL |
| user_id | BIGINT | FK → users(id), NOT NULL |

#### Constraints

(user_id, name) must be unique.

### tags

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, Auto Increment |
| name | VARCHAR(50) | NOT NULL |
| user_id | BIGINT | FK → users(id), NOT NULL |

#### Constraints

(user_id, name) must be unique.

### future_letters

| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PK, Auto Increment |
| title | VARCHAR(150) | NOT NULL |
| content | TEXT | NOT NULL |
| unlock_at | TIMESTAMP | NOT NULL |
| is_opened | BOOLEAN | NOT NULL |
| created_at | TIMESTAMP | NOT NULL |
| user_id | BIGINT | FK → users(id), NOT NULL |

### article_tags

| Column | Type | Constraints |
|--------|------|-------------|
| article_id | BIGINT | FK → articles(id), NOT NULL |
| tag_id | BIGINT | FK → tags(id), NOT NULL |

#### Constraints

(article_id, tag_id) must be unique.

## Relationships

User (1) → (N) Articles

User (1) → (N) Categories

User (1) → (N) Tags

User (1) → (N) Future Letters

Category (1) → (N) Articles

Article (N) → (N) Tags (via article_tags)

## Design Decisions

Each user has their own categories.

Each user has their own tags.

Article titles are not required to be unique.

Usernames and emails must be unique.

Category names must be unique per user.

Tag names must be unique per user.

Future letters are independent from articles.

The first version supports one author per article.

The architecture should allow collaborative articles in future versions.

## Notes

This database design represents Version 1 (MVP).

Additional features such as favorites, file uploads, article versioning, reminders, public profiles, and collaboration will be introduced in future versions.