# java-filmorate

### DB Scheme

![DBScheme.png](src/Files/DBScheme.png)

### Database entity discription

##### User - Stores information about all users

| № | Column Name | Description      | Type    | Key | Reference |
|---|-------------|------------------|---------|-----|-----------|
| 1 | User_Id     | Identifier       | integer | PK  |           |
| 2 | Email       | Email            | varchar |     |           |
| 3 | Login       | Login            | varchar |     |           |
| 4 | Name        | Name             | varchar |     |           |
| 5 | BirthDate   | User`s Birthdate | date    |     |           |

##### Film - Stores information about all films

| № | Column Name | Description      | Type    | Key | Reference |
|---|-------------|------------------|---------|-----|-----------|
| 1 | id          | Identifier       | integer | PK  |           |
| 2 | Name        | Film Name        | varchar |     |           |
| 3 | Description | Description      | varchar |     |           |
| 4 | ReleaseDate | Release Date     | date    |     |           |
| 5 | Duration    | Duration in mins | integer |     |           |
| 6 | Rating_id   | Rating           | integer | FK  | Rating    |

##### Friendship - Intersection table between users friend requests

| № | Column Name | Description       | Type    | Key | Reference |
|---|-------------|-------------------|---------|-----|-----------|
| 1 | id          | Identifier        | integer | PK  |           |
| 2 | User_id     | User Identifier   | integer | FK  | User      |
| 3 | Friend_id   | Friend Identifier | integer | FK  | User      |

---

##### Like - Stores all user`s film likes

| № | Column Name | Description     | Type    | Key | Reference |
|---|-------------|-----------------|---------|-----|-----------|
| 1 | id          | Identifier      | integer | PK  |           |
| 2 | Film_id     | Film Identifier | integer | FK  | Film      |
| 3 | User_id     | User Identifier | integer | FK  | User      |

---

##### FilmGenre - Stores all film`s genres

| № | Column Name | Description      | Type    | Key | Reference |
|---|-------------|------------------|---------|-----|-----------|
| 1 | Id          | Identifier       | integer | PK  |           |
| 2 | Film_id     | Film Identifier  | integer | FK  | Film      |
| 3 | Genre_id    | Genre Identifier | integer | FK  | Genre     |

##### Genre - List of all Genres

| № | Column Name | Description  | Type    | Key | Reference |
|---|-------------|--------------|---------|-----|-----------|
| 1 | id          | Identifier   | integer | PK  |           |
| 2 | Name        | Genre`s name | varchar |     |           |

---

##### Rating - List of all ratings

| № | Column Name | Description | Type    | Key | Reference |
|---|-------------|-------------|---------|-----|-----------|
| 1 | id          | Identifier  | integer | PK  |           |
| 2 | Code        | Rating code | varchar |     |           |
| 3 | Description | Description | varchar |     |           |

---

### Query examples

##### Query for get all users

````sql
Select *
from users
````

##### Query for specific user fields

````sql
SELECT u.ID,
       u.LOGIN,
       u.EMAIL
FROM USERS u
WHERE u.ID = ?
````

##### Query for amount of film likes

````sql
SELECT count(*)
FROM LIKES l
WHERE l.FILM_ID = ?
````

##### Query for all user`s friends

````sql
SELECT *
FROM USERS u
WHERE u.ID in
      (SELECT f.FRIEND_ID
       FROM FRIENDSHIP f
       WHERE f.USER_ID = ?)
````

#### Query for all users and their friends

````sql
SELECT u.ID        as user_id,
       u.name      as user_name,
       u.LOGIN     as user_login,
       u.EMAIL     as user_email,
       u.BIRTHDATE as user_birthdate,
       f.USER_ID   as friend_id
FROM USERS u
         LEFT JOIN FRIENDSHIP f on f.FRIEND_ID = u.ID
````