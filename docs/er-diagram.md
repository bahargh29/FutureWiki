# FutureWiki ER Diagram

## Version

Current Version: 1.0

Last Updated: 2026-06-30

Status: Approved

---

## Entity Relationship Diagram

```mermaid
erDiagram

    USER {
        id PK
        username
        email
        password
        role
        created_at
    }

    CATEGORY {
        id PK
        name
        user_id FK
    }

    TAG {
        id PK
        name
        user_id FK
    }

    ARTICLE {
        id PK
        title
        content
        created_at
        updated_at
        user_id FK
        category_id FK
    }

    FUTURE_LETTER {
        id PK
        title
        content
        unlock_at
        is_opened
        created_at
        user_id FK
    }

    ARTICLE_TAG {
        article_id FK
        tag_id FK
    }

    USER ||--o{ ARTICLE : owns
    USER ||--o{ CATEGORY : owns
    USER ||--o{ TAG : owns
    USER ||--o{ FUTURE_LETTER : owns

    CATEGORY ||--o{ ARTICLE : contains

    ARTICLE ||--o{ ARTICLE_TAG : has
    TAG ||--o{ ARTICLE_TAG : has
```