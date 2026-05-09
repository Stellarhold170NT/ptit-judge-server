# Roadmap: Java Judge Server

**Project:** Java Judge Server — PTIT Luyện Tập  
**Created:** 2025-05-09  
**Mode:** mvp  
**Total Phases:** 6  
**Total Requirements:** 33

---

### Phase 1: Project Setup & Auth Foundation
**Goal:** Khởi tạo Spring Boot project, JSON storage layer, auth system, và web UI cơ bản.
**Mode:** mvp
**Success Criteria**:
1. `mvn spring-boot:run` khởi động server thành công
2. User có thể đăng ký, đăng nhập, đăng xuất qua web UI
3. Dữ liệu user được lưu và đọc từ JSON file
4. Có trang chủ cơ bản hiển thị sau khi đăng nhập

**Requirements:** AUTH-01, AUTH-02, AUTH-03, DATA-01, DATA-02, DATA-04, DATA-05, UI-01, UI-02, DEP-02, DEP-03

**Canonical refs:**
- `.planning/PROJECT.md` §Constraints (tech stack decisions)
- `luyentap/1.md`–`9.md` (exercise specifications)

---

### Phase 2: REST Services — Bài 1–3
**Goal:** Triển khai 6 REST endpoint cho 3 bài tập đầu tiên (tính tổng, sắp xếp chuỗi, tính giá sản phẩm).
**Mode:** mvp
**Success Criteria**:
1. GET/POST `/api/rest/data` hoạt động đúng, trả về AC/WA
2. GET/POST `/api/rest/character` hoạt động đúng với case-sensitive sort
3. GET/POST `/api/rest/object` hoạt động đúng với tolerance ≤ 0.01
4. Mỗi request tạo requestId duy nhất và data ngẫu nhiên
5. Submit được lưu vào lịch sử JSON

**Requirements:** REST-01, REST-02, REST-03, REST-04, REST-05, REST-06, DATA-03

**Canonical refs:**
- `luyentap/1.md` (REST data sum)
- `luyentap/2.md` (REST character sort)
- `luyentap/3.md` (REST object calculation)

---

### Phase 3: gRPC Services — Bài 4–6
**Goal:** Triển khai gRPC JudgeService với proto contract cho 3 bài tập (tổng, sort, object).
**Mode:** mvp
**Success Criteria**:
1. Proto file đúng spec (package `GRPC`, field numbers giữ nguyên)
2. `Request` và `Submit` RPC hoạt động cho cả 3 bài
3. Java client mẫu có thể kết nối và nhận AC/WA/RTE
4. gRPC server chạy trên port 2240 (hoặc cấu hình được)

**Requirements:** GRPC-01, GRPC-02, GRPC-03, GRPC-04, GRPC-05, GRPC-06, GRPC-07

**Canonical refs:**
- `luyentap/4.md` (gRPC data sum)
- `luyentap/5.md` (gRPC word sort)
- `luyentap/6.md` (gRPC object calculation)

---

### Phase 4: SOAP Services — Bài 7–9
**Goal:** Triển khai 3 SOAP service với WSDL cho DataService, CharacterService, ObjectService.
**Mode:** mvp
**Success Criteria**:
1. WSDL có thể truy cập tại `/DataService?wsdl`, `/CharacterService?wsdl`, `/ObjectService?wsdl`
2. `getData`/`submitDataInt` hoạt động đúng (Bài 7)
3. `requestString`/`submitString` hoạt động đúng (Bài 8)
4. `requestProductY`/`submitProductY` hoạt động đúng (Bài 9)
5. Java client dùng `wsimport` có thể tạo stub và gọi service

**Requirements:** SOAP-01, SOAP-02, SOAP-03, SOAP-04, SOAP-05, SOAP-06

**Canonical refs:**
- `luyentap/7.md` (SOAP data sum)
- `luyentap/8.md` (SOAP string reverse)
- `luyentap/9.md` (SOAP object calculation)

---

### Phase 5: Web UI & Dashboard
**Goal:** Hoàn thiện giao diện web cho sinh viên xem bài tập, submit, và theo dõi tiến độ.
**Mode:** mvp
**Success Criteria**:
1. Dashboard hiển thị 9 bài tập với badge REST/gRPC/SOAP
2. Trang chi tiết mỗi bài hiển thị endpoint, ví dụ request/response
3. Sinh viên có thể xem lịch sử submit cá nhân
4. Progress indicator hiển thị số bài đã pass
5. UI responsive cơ bản (dùng được trên mobile)

**Requirements:** UI-03, UI-04, UI-05, UI-06

**Canonical refs:**
- Phase 1–4 implementations (endpoint details)

---

### Phase 6: Answer Code, Docker & Deployment
**Goal:** Cung cấp code đáp án cho cả 9 bài, Docker hóa, và hướng dẫn deploy DigitalOcean.
**Mode:** mvp
**Success Criteria**:
1. Thư mục `answers/` chứa Java client cho cả 9 bài, build và chạy được
2. `Dockerfile` build image Spring Boot server
3. `docker-compose.yml` chạy server + mount JSON volume
4. `DEPLOY.md` hướng dẫn từng bước deploy lên DigitalOcean droplet
5. `README.md` đầy đủ hướng dẫn quick start

**Requirements:** DEP-01, DEP-04, DEP-05

**Canonical refs:**
- Phase 2–4 service implementations (để viết client đúng)
- `.planning/PROJECT.md` §Constraints (DigitalOcean deployment)

---

## Dependency Graph

```
Phase 1 ──► Phase 2 ──► Phase 3 ──► Phase 4 ──► Phase 5 ──► Phase 6
 (Setup)    (REST)     (gRPC)      (SOAP)       (UI)      (Deploy)
```

**Sequential by design:** Mỗi phase xây dựng trên phase trước. Phase 5 (UI) cần tất cả service đã hoạt động. Phase 6 (Deploy) cần toàn bộ app hoàn chỉnh.

**Internal parallelization:** Trong Phase 2–4, các endpoint riêng lẻ có thể phát triển song song.

---

## Change Log

| Date | Change | By |
|------|--------|-----|
| 2025-05-09 | Initial roadmap created | Initialization |

---
*Roadmap created: 2025-05-09*
