# Pet Clinic Management System — Backend

A Spring Boot REST API backend for managing a veterinary clinic: veterinarians, exam rooms, expertises, pets, consultations, and medical records. Built as the AUCA Web Technology mid-term project.

## Student Information

| Field | Value |
|---|---|
| Student Name | IRADUKUNDA SHEMA Jean Serge |
| Student ID | 26882 |
| University | Adventist University of Central Africa (AUCA) |
| Project Name | Pet Clinic Management System |

## Technologies and Tools

- **Java 21**
- **Spring Boot 3.3.5** (Spring Web, Spring Data JPA)
- **PostgreSQL 17**
- **Maven** (build tool)
- **Postman** (API testing / submission collection)
- Hibernate ORM (via Spring Data JPA), `spring.jpa.hibernate.ddl-auto=update` for automatic schema management

## System Entities and Relationships

The system has six JPA entities:

| Entity | Key fields | Relationships |
|---|---|---|
| **Expertise** | `id`, `name` | `@ManyToMany(mappedBy = "expertises")` back-reference to `Veterinarian`, annotated `@JsonIgnore` to prevent infinite JSON recursion |
| **ExamRoom** | `id`, `roomNumber`, `name` | No direct relationships (referenced by `Veterinarian`) |
| **Veterinarian** | `id`, `firstName`, `lastName`, `licenseNumber` | `@ManyToOne` to `ExamRoom` (column `exam_room_id`, nullable); owning side of `@ManyToMany` to `Expertise` via join table `veterinarian_expertise` |
| **Pet** | `id`, `petName`, `ownerName`, `tagNumber` (unique), `species`, `ownerPhone` | No direct relationships (referenced by `Consultation`) |
| **Consultation** | `id`, `visitDate`, `status` (`ConsultationStatus` enum, stored as `STRING`), `reason` | `@ManyToOne` to `Pet` (column `pet_id`) and `@ManyToOne` to `Veterinarian` (column `veterinarian_id`) |
| **MedicalRecord** | `id`, `procedure`, `notes`, `doseMg`, `recordedDate` | `@ManyToOne` to `Consultation` (column `consultation_id`) — one-directional; `Consultation` has no back-reference |

`ConsultationStatus` enum values: `BOOKED`, `ARRIVED`, `TREATED`, `MISSED`.

All primary keys are `UUID`, generated via `@GeneratedValue(strategy = GenerationType.UUID)`. Tables are created/updated automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

## Implemented Requirements

### Part A — Entities, Relationships, and CRUD

| ID | Description |
|---|---|
| **A1** | Full entity model: all five core entities, the `ConsultationStatus` enum, required fields, JPA annotations, PostgreSQL tables/foreign keys, the `veterinarian_expertise` join table, and `@JsonIgnore` on `Expertise.veterinarians` to prevent JSON recursion. |
| **A2** | Standard CRUD for all five entities (Expertise, ExamRoom, Veterinarian, Pet, Consultation): `POST /api/{path}/save`, `GET /api/{path}/all`, `GET /api/{path}/{id}`, `PUT /api/{path}/update/{id}`, `DELETE /api/{path}/delete/{id}`. |

### Part B — Derived Queries

| ID | Endpoint | Description |
|---|---|---|
| **B1** | `GET /api/pets/by-species?species=` | Case-insensitive species match, sorted by pet name ascending (derived query `findBySpeciesIgnoreCaseOrderByPetNameAsc`). |
| **B2** | `GET /api/consultations/by-status?status=` | Consultations filtered by exact `ConsultationStatus`, sorted by visit date ascending. |
| **B3** | `GET /api/consultations/between?start=&end=` | Consultations with `visitDate` inclusively between two dates. |

### Part C — JPQL Queries

| ID | Endpoint | Description |
|---|---|---|
| **C1** | `GET /api/veterinarians/by-expertise?name=` | JPQL `JOIN` + `LOWER` + `LIKE` for a case-insensitive partial match on expertise name, `DISTINCT` to avoid duplicates. |
| **C2** | `GET /api/exam-rooms/free` | JPQL `NOT EXISTS` query returning exam rooms with no assigned veterinarian, ordered by room number. |
| **C3** | `GET /api/expertises/unused` | JPQL query using `IS EMPTY` on the `veterinarians` collection to find expertises held by no veterinarian. |
| **C4** | `GET /api/pets/of-veterinarian/{veterinarianId}` | JPQL `DISTINCT` query navigating `Consultation → Pet` to list every distinct pet seen by a veterinarian; 404 if the veterinarian does not exist. |

### Part D — Business Rules

| ID | Endpoint | Description |
|---|---|---|
| **D1** | `POST /api/consultations/save` (hardened) | Rejects a `visitDate` in the past (400) and a duplicate active consultation for the same veterinarian/date (409); always forces the new consultation's status to `BOOKED` regardless of the request body. |
| **D2** | `PATCH /api/consultations/{id}/status?status=` | Enforces the status state machine: `BOOKED → ARRIVED/MISSED`, `ARRIVED → TREATED/MISSED`; `TREATED`/`MISSED` are terminal. Invalid transitions return 400; the general `PUT /update/{id}` can never change status. |
| **D3** | `PUT /api/veterinarians/{veterinarianId}/expertises/{expertiseId}` | Adds an expertise to a veterinarian without removing existing ones; 404 for missing veterinarian/expertise, 409 if already held. |
| **D4** | `PATCH /api/veterinarians/{veterinarianId}/examroom?roomNumber=` | Assigns an exam room to a veterinarian, automatically freeing their previous room; 404 for missing veterinarian/room, 409 if the room is already taken by another veterinarian. |

### Part E — Medical Records

| ID | Endpoint | Description |
|---|---|---|
| **E1** | `POST /api/medical-records/save?consultationId=` | Creates a medical record linked to a consultation; requires the consultation to exist (404) and be `TREATED` (400); `recordedDate` is always set to the current date. |
| **E2** | `GET /api/medical-records/pet/{tagNumber}` | JPQL path navigation `MedicalRecord → Consultation → Pet`, filtered by `tagNumber`, sorted by `recordedDate` descending (newest first). |
| **E3** | `DELETE /api/medical-records/delete/{id}` | Deletes a medical record (404 if missing). The general consultation delete (`DELETE /api/consultations/delete/{id}`) was also extended to return 409 if the consultation still has medical records attached. |

### Part F — Reports and Bulk Actions

| ID | Endpoint | Description |
|---|---|---|
| **F1** | `GET /api/consultations/stats/by-status` | JPQL `GROUP BY` + `COUNT`, returning one `[status, count]` pair per status present. |
| **F2** | `PATCH /api/consultations/cancel-day?veterinarianId=&date=` | JPQL bulk `@Modifying @Transactional UPDATE` that marks every non-`TREATED` consultation for a veterinarian on a given date as `MISSED`, in a single query. |

## Bonus Features

| Feature | Endpoint | Description |
|---|---|---|
| **Dashboard Summary** | `GET /api/dashboard/summary` | Returns a single JSON object (`LinkedHashMap`, fixed key order) with six aggregate counts: `pets`, `veterinarians`, `freeExamRooms`, `consultationsToday`, `booked`, `medicalRecords`. |
| **Consultation Pagination** | `GET /api/consultations/page?page=&size=&sort=field,dir` | Returns a Spring Data `Page<Consultation>` using the inherited `JpaRepository.findAll(Pageable)` — standard page metadata (`content`, `totalElements`, `totalPages`, `number`, `size`, etc.), supports `asc`/`desc` sorting. |

## Database Setup

1. Install and run **PostgreSQL 17** locally.
2. Create the database used by this application:
   ```sql
   CREATE DATABASE petclinic;
   ```
3. No seed script is required to run the application — Hibernate creates all tables automatically on first startup (`spring.jpa.hibernate.ddl-auto=update`).

## Application Configuration

Database connection settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/petclinic
spring.datasource.username=<your-postgresql-username>
spring.datasource.password=<your-postgresql-password>

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Replace `<your-postgresql-username>` and `<your-postgresql-password>` with your own local PostgreSQL credentials before running the application. **Do not commit real credentials to version control.**

## Build, Test, and Run

From the project root (`pet-clinic-management-system/`):

```bash
# Build the project and download dependencies
mvn clean install

# Run the test suite
mvn test

# Start the application (listens on http://localhost:8080)
mvn spring-boot:run
```

Alternatively, after `mvn clean install`, the packaged jar can be run directly:

```bash
java -jar target/pet-clinic-management-system-0.0.1-SNAPSHOT.jar
```

## Postman Collection

A complete Postman collection is provided at the project root:

```
pet-clinic-management-system-midterm.postman_collection.json
```

It contains one request per graded requirement (A1, A2, B1–B3, C1–C4, D1–D4, E1–E3, F1–F2, BONUS-1, BONUS-2) — 20 requests in total, each with a description documenting a success scenario, an applicable error scenario, and any prerequisites.

**To import:**
1. Open Postman → **Import** → select `pet-clinic-management-system-midterm.postman_collection.json`.
2. Open the collection's **Variables** tab and confirm `baseUrl` is set to `http://localhost:8080` (adjust if your server runs elsewhere).
3. Run requests **individually**, in the order they appear — do **not** use Postman's "Run collection" feature to fire all 20 automatically, since some requests intentionally depend on data created by earlier ones.

The collection already includes real seed UUIDs and tag numbers (e.g. for the veterinarians Jean, Claire, and Eric, and the pets Rex, Bella, Whiskers, Tweety, and Max) as collection variables, so most requests work out of the box against a freshly seeded database.

## Example API Requests

**Create an exam room (Part A2):**
```http
POST /api/exam-rooms/save
Content-Type: application/json

{
  "roomNumber": 5,
  "name": "Recovery Room"
}
```
→ `201 Created`, `{"message":"ExamRoom saved successfully","data":{...}}`

**Get a pet by ID, missing case (Part A2):**
```http
GET /api/pets/00000000-0000-0000-0000-000000000000
```
→ `404 Not Found`, `{"message":"The pet with that id does not exist"}`

**Derived query — pets by species (Part B1):**
```http
GET /api/pets/by-species?species=dog
```
→ `200 OK`, array of pets whose species matches "dog" (case-insensitive), sorted by name.

**JPQL query — free exam rooms (Part C2):**
```http
GET /api/exam-rooms/free
```
→ `200 OK`, array of exam rooms with no veterinarian assigned.

**Business rule — reject a past-dated consultation (Part D1):**
```http
POST /api/consultations/save
Content-Type: application/json

{
  "visitDate": "2020-01-01",
  "reason": "Routine checkup",
  "pet": { "id": "<pet-uuid>" },
  "veterinarian": { "id": "<veterinarian-uuid>" }
}
```
→ `400 Bad Request`, `{"message":"Consultation date cannot be in the past"}`

**Bulk update — cancel a day's consultations (Part F2):**
```http
PATCH /api/consultations/cancel-day?veterinarianId=<uuid>&date=2026-12-01
```
→ `200 OK`, `{"message":"Consultations for that day marked as MISSED successfully","updatedCount":2}`

**Dashboard summary (Bonus 1):**
```http
GET /api/dashboard/summary
```
→ `200 OK`
```json
{
  "pets": 5,
  "veterinarians": 3,
  "freeExamRooms": 1,
  "consultationsToday": 0,
  "booked": 3,
  "medicalRecords": 0
}
```

**Paginated consultations (Bonus 2):**
```http
GET /api/consultations/page?page=0&size=5&sort=visitDate,desc
```
→ `200 OK`, a `Page<Consultation>` object with `content`, `totalElements`, `totalPages`, `number`, `size`, etc.

## Temporary Demonstration Records and Cleanup

Several Postman requests intentionally create temporary records in order to safely demonstrate business rules without ever touching the permanent seed data:

- **A2** (`POST /api/veterinarians/save`) creates a temporary veterinarian, whose ID is captured for reuse by **D3** and **D4**.
- **D1** (`POST /api/consultations/save`) creates a temporary consultation on a safe future date that cannot collide with existing data; it is reused by **D2** and **F2**.
- **E1** (`POST /api/medical-records/save`) attaches a new medical record to an existing `TREATED` seed consultation (it does not modify the consultation itself).

After demonstrating the full D3/D4 and E1/E3 flow, clean up with:

```http
DELETE /api/veterinarians/delete/{tempVeterinarianId}
DELETE /api/consultations/delete/{tempConsultationId}
DELETE /api/medical-records/delete/{tempMedicalRecordId}
```

(the exact IDs are auto-captured into Postman collection variables as each request runs). Running these cleanup calls restores the database to its original seeded state. **Never** run the add-expertise (D3), assign-room (D4), or cancel-day (F2) requests against the permanent seed veterinarians or seed dates — doing so would permanently alter the grading dataset.
