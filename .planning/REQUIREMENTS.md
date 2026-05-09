# Requirements: Java Judge Server

**Defined:** 2025-05-09
**Core Value:** Sinh viên có thể luyện tập 9 bài tập mẫu (REST/gRPC/SOAP) trên một server local/deploy được, với đầy đủ code đáp án mẫu và lệnh chạy ngay.

## v1 Requirements

### Authentication

- [ ] **AUTH-01**: User can register with email and password
- [ ] **AUTH-02**: User can log in and receive a session token (JWT or simple token)
- [ ] **AUTH-03**: User can log out and invalidate session

### REST Services (Bài 1–3)

- [ ] **REST-01**: Server exposes `GET /api/rest/data?studentCode=&qCode=` returning `{"requestId":"...","data":[integers]}`
- [ ] **REST-02**: Server exposes `POST /api/rest/data/submit` accepting `{"studentCode":"...","qCode":"...","requestId":"...","answer":<sum>}`, returns `{"status":"AC|WA"}`
- [ ] **REST-03**: Server exposes `GET /api/rest/character?studentCode=&qCode=` returning `{"requestId":"...","data":"word list"}`
- [ ] **REST-04**: Server exposes `POST /api/rest/character/submit` accepting sorted words (case-sensitive), returns AC/WA
- [ ] **REST-05**: Server exposes `GET /api/rest/object?studentCode=&qCode=` returning `{"requestId":"...","data":{product}}`
- [ ] **REST-06**: Server exposes `POST /api/rest/object/submit` accepting product with `finalPrice`, returns AC/WA (tolerance ≤ 0.01)

### gRPC Services (Bài 4–6)

- [ ] **GRPC-01**: Server implements `JudgeService` with `Request` and `Submit` RPCs (proto package `GRPC`)
- [ ] **GRPC-02**: `Request` returns `request_id` + comma-separated integers for Bài 4 (`qCode` = `1zog4SNL` equivalent)
- [ ] **GRPC-03**: `Submit` validates sum answer for Bài 4, returns `{"status":"AC|WA|RTE","message":"..."}`
- [ ] **GRPC-04**: `Request` returns comma-separated words for Bài 5
- [ ] **GRPC-05**: `Submit` validates case-insensitive sorted words for Bài 5
- [ ] **GRPC-06**: `Request` returns JSON product string for Bài 6
- [ ] **GRPC-07**: `Submit` validates `finalPrice = price*(1+taxRate/100)-discount`, rounded 2 decimals

### SOAP Services (Bài 7–9)

- [ ] **SOAP-01**: Server exposes `DataService` WSDL with `getData(studentCode,qCode)` returning `List<Integer>`
- [ ] **SOAP-02**: `submitDataInt(studentCode,qCode,sum)` validates answer, returns status string AC/WA (Bài 7)
- [ ] **SOAP-03**: Server exposes `CharacterService` WSDL with `requestString(studentCode,qCode)` returning string
- [ ] **SOAP-04**: `submitString(studentCode,qCode,reversed)` validates reversed string (Bài 8)
- [ ] **SOAP-05**: Server exposes `ObjectService` WSDL with `requestProductY(studentCode,qCode)` returning `ProductY`
- [ ] **SOAP-06**: `submitProductY(studentCode,qCode,productY)` validates `finalPrice` calculation (Bài 9)

### Data & Scoring Engine

- [ ] **DATA-01**: Server generates unique `requestId` (UUID or random string) for every request
- [ ] **DATA-02**: Server generates random exercise data (integers, words, product values) per request
- [ ] **DATA-03**: Server computes correct answer internally and compares with submission (tolerance ≤ 0.01 for floats)
- [ ] **DATA-04**: Server stores user accounts (email, hashed password, studentCode) in JSON file
- [ ] **DATA-05**: Server stores submission history (timestamp, exercise, status, answer) in JSON file

### Web UI

- [ ] **UI-01**: User can register via web form (email, password, studentCode)
- [ ] **UI-02**: User can log in via web form
- [ ] **UI-03**: Dashboard shows list of 9 exercises with protocol badges (REST/gRPC/SOAP)
- [ ] **UI-04**: Exercise detail page shows endpoint info, sample request/response, and submission form
- [ ] **UI-05**: User can view personal submission history with AC/WA status
- [ ] **UI-06**: Simple progress indicator (e.g., 6/9 passed)

### Answer Code & Deployment

- [ ] **DEP-01**: Repository includes Java client answer code for all 9 exercises (under `answers/`)
- [ ] **DEP-02**: `mvn spring-boot:run` starts server locally without extra setup
- [ ] **DEP-03**: `README.md` includes quick start guide and endpoint documentation
- [ ] **DEP-04**: Repository includes `Dockerfile` and `docker-compose.yml` for easy deployment
- [ ] **DEP-05**: `DEPLOY.md` includes step-by-step DigitalOcean deployment guide

## v2 Requirements

### Leaderboard & Admin

- **LEAD-01**: Global leaderboard showing top students by AC count
- **ADMIN-01**: Admin panel to view all submissions
- **ADMIN-02**: Admin can reset exercise data or regenerate random seeds

### Enhanced Features

- **ENH-01**: Rate limiting per student to prevent spam
- **ENH-02**: Exercise timer (time limit per submission attempt)
- **ENH-03**: Export submission history to CSV

## Out of Scope

| Feature | Reason |
|---------|--------|
| Compile & run student Java code on server | Too complex, requires sandbox/container isolation; current exam server only compares output |
| Real-time collaborative features | Not core to practice value |
| OAuth / Google / GitHub login | Local auth sufficient for v1 |
| Mobile app | Web-first, responsive UI is enough |
| PostgreSQL / MySQL database | Explicit requirement: JSON file only |
| Automatic code plagiarism detection | Out of scope for practice server |

## Traceability

| Requirement | Phase | Status |
|-------------|-------|--------|
| AUTH-01 | Phase 1 | Pending |
| AUTH-02 | Phase 1 | Pending |
| AUTH-03 | Phase 1 | Pending |
| REST-01 | Phase 2 | Pending |
| REST-02 | Phase 2 | Pending |
| REST-03 | Phase 2 | Pending |
| REST-04 | Phase 2 | Pending |
| REST-05 | Phase 2 | Pending |
| REST-06 | Phase 2 | Pending |
| GRPC-01 | Phase 3 | Pending |
| GRPC-02 | Phase 3 | Pending |
| GRPC-03 | Phase 3 | Pending |
| GRPC-04 | Phase 3 | Pending |
| GRPC-05 | Phase 3 | Pending |
| GRPC-06 | Phase 3 | Pending |
| GRPC-07 | Phase 3 | Pending |
| SOAP-01 | Phase 4 | Pending |
| SOAP-02 | Phase 4 | Pending |
| SOAP-03 | Phase 4 | Pending |
| SOAP-04 | Phase 4 | Pending |
| SOAP-05 | Phase 4 | Pending |
| SOAP-06 | Phase 4 | Pending |
| DATA-01 | Phase 1 | Pending |
| DATA-02 | Phase 1 | Pending |
| DATA-03 | Phase 2 | Pending |
| DATA-04 | Phase 1 | Pending |
| DATA-05 | Phase 1 | Pending |
| UI-01 | Phase 1 | Pending |
| UI-02 | Phase 1 | Pending |
| UI-03 | Phase 5 | Pending |
| UI-04 | Phase 5 | Pending |
| UI-05 | Phase 5 | Pending |
| UI-06 | Phase 5 | Pending |
| DEP-01 | Phase 6 | Pending |
| DEP-02 | Phase 1 | Pending |
| DEP-03 | Phase 1 | Pending |
| DEP-04 | Phase 6 | Pending |
| DEP-05 | Phase 6 | Pending |

**Coverage:**
- v1 requirements: 33 total
- Mapped to phases: 33
- Unmapped: 0 ✓

---
*Requirements defined: 2025-05-09*
*Last updated: 2025-05-09 after initial definition*
