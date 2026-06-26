# FutureWiki Requirements
## 1. Project Vision

FutureWiki is a personal knowledge management platform that helps users organize their knowledge, experiences, goals, ideas, and future plans in a wiki-style environment.

The platform also allows users to write letters to their future selves and unlock them on a specified date.

The goal of FutureWiki is to become a personal digital knowledge base that grows together with its user.

FutureWiki aims to provid a single place where users can preserve their past, organize their present, and plan their future.

---

## 2. Target Users

FutureWiki is designed for:

- Students
- Software Developers
- Lifelong Learners
- People interested in personal growth
- Anyone who wants to organize personal knowledge and memories

---

## 3. Functional Requirements

### Authentication
- User registration
- User login
- JWT-based authentication
- Secure password storage

### Articles
- Create an article
- View articles
- View a single article
- Edit an article
- Delete an article

### Categories
- Create categories
- Assign a category to an article

### Tags
- Create tags
- Assign multiple tags to an article

### Future Letters
- Create a future letter
- Set an unlock date
- View unlocked letters
- Prevent access before the unlock date

### Search
- Search articles by title
- Search articles by content
- Search by tags
- Search categories

### Dashboard
- Display total number of articles
- Display total number of future letters
- Display recent articles
- Display upcoming future letters

---

## 4. Non-Functional Requirements

- RESTful API design
- Secure authentication using JWT
- Clean and maintainable code
- Layered architecture
- Input validation
- Global exception handling
- Logging
- API documentation
- Scalable project structure
- Database normalization
- Consistent API response structure
- Role-based authorization

---

## 5. Out of Scope

The following features are not included in Version 1:

- Public profiles
- Social features
- AI-powered search
- AI-generated summaries
- Email reminders
- Mobile application
- File uploads
- Article version history

---

## 6. MVP Features

Version 1 includes:

- User Authentication
- JWT Security
- Article Management
- Categories
- Tags
- Future Letters
- Search
- Dashboard