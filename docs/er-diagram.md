# FutureWiki ER Diagram

## Version

Current Version: 1.0

Last Updated: 2026-06-30

Status: Approved

---

## Entity Relationship Diagram

mermaid
erDiagram

    USER {
        BIGINT id PK
        VARCHAR username
        VARCHAR email
        VARCHAR password
        ENUM role
        TIMESTAMP created_at
    }

    CATEGORY {
        BIGINT id PK
        VARCHAR name
        BIGINT user_id FK
    }

    TAG {
        BIGINT id PK
        VARCHAR name
        BIGINT user_id FK
    }

    ARTICLE {
        BIGINT id PK
        VARCHAR title
        TEXT content
        TIMESTAMP created_at
        TIMESTAMP updated_at
        BIGINT user_id FK
        BIGINT category_id FK
    }

    FUTURE_LETTER {
        BIGINT id PK
        VARCHAR title
        TEXT content
        TIMESTAMP unlock_at
        BOOLEAN is_opened
        TIMESTAMP created_at
        BIGINT user_id FK
    }

    ARTICLE_TAG {
        BIGINT article_id FK
        BIGINT tag_id FK
    }

    USER ||--o{ ARTICLE : owns
    USER ||--o{ CATEGORY : owns
    USER ||--o{ TAG : owns
    USER ||--o{ FUTURE_LETTER : owns

    CATEGORY ||--o{ ARTICLE : contains

    ARTICLE ||--o{ ARTICLE_TAG : has
    TAG ||--o{ ARTICLE_TAG : has