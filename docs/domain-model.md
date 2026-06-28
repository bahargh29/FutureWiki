# FutureWiki Domain Model

## Version

Current Version: 1.0
Last Updated: 2026-06-28
Status: Approved

---

## Entities

### User

Represents a registered user who can manage personal knowledge and future letters.

### Article

Represents a knowledge article created
by a user.

### Category

Represents a category used to organize articles.

### Tag

Represents a keyword that can be assigned to multiple articles.

### FutureLetter

Represents a letter written by a user that becomes accessible on a specified data.

---

## Relationships

### User -> Article

- One User can create multiple Articles.
- Each Article belongs to exactly one User.

Relationship: One-to-Many (1:N)

Future consideration:

Articles may support multiple collaborators in future versions.

---

### User -> Category

- One User can create multiple Categories.
- Each Category belongs to exactly one User.

Relationship: One-to-Many (1:N)

---

### User -> Tag

- One User can create multiple Tags.
- Each Tag belongs to exactly one User.

Relationship: One-to-Many (1:N)

### User -> FutureLetter

- One User can create multiple Future Letters.
- Each Future Letter belongs to exactly one User.

Relationship: One-to-Many (1:N)

---

### Category -> Article

- One Category can contain multiple Articles.
- Each Article belongs to one Category.

Relationship: One-to-Many (1:N)

---

### Article -> Tag

- One Article can have multiple Tags.
- One Tag can be assigned to multiple Articles.

Relationship: Many-to-Many (M:N)

---

## Entity Attributes

### User

- id
- username
- email
- password
- role
- createdAt

---

### Article

- id
- title
- content
- createdAt
- updatedAt

---

### Category

- id
- name

---

### Tag

- id
- name

---

### FutureLetter

- id
- title
- content
- unlockAt
- isOpened
- createdAt

---

## Design Decisions

- Each user manages their own categories.
- Each article belongs to exactly one category.
- Tags are shared within a user's articles for flrxible organization.
- Future letters are independent from articles.
- The initial version supports only one author per article.
- The architecture should allow collaborative articles in future versions.