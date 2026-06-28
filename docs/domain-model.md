# FutureWiki Domain Model

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

Articles may support multiple collaborators in future version.

---

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