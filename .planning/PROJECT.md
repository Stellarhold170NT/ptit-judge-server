# Java Judge Server — PTIT Luyện Tập

## What This Is

Một hệ thống chấm điểm (judge server) viết bằng Java, triển khai trên DigitalOcean, dành cho sinh viên PTIT luyện tập 9 bài tập mẫu phân bổ trên 3 giao thức: REST, gRPC, và SOAP. Server cung cấp endpoint cho từng bài tập, nhận kết quả từ sinh viên, so sánh với đáp án, và trả về trạng thái AC/WA/RTE. Hệ thống bao gồm web UI đơn giản để quản lý tài khoản, submit bài, và xem kết quả. Không dùng database — toàn bộ dữ liệu lưu bằng JSON file.

## Core Value

Sinh viên có thể luyện tập 9 bài tập mẫu (REST/gRPC/SOAP) trên một server local/deploy được, với đầy đủ code đáp án mẫu và lệnh chạy ngay.

## Requirements

### Validated

(None yet — ship to validate)

### Active

- [ ] Server cung cấp 3 REST endpoint (Bài 1–3) với request/submit flow
- [ ] Server cung cấp 3 gRPC service (Bài 4–6) với JudgeService proto
- [ ] Server cung cấp 3 SOAP service (Bài 7–9) với WSDL
- [ ] Hệ thống tạo requestId duy nhất và data ngẫu nhiên cho mỗi lần gọi
- [ ] Hệ thống so sánh kết quả submit với đáp án và trả về AC/WA/RTE
- [ ] Quản lý tài khoản (đăng ký, đăng nhập, session) bằng JSON file
- [ ] Lưu trữ lịch sử submit và điểm số bằng JSON file
- [ ] Web UI đơn giản cho đăng ký, đăng nhập, submit, xem kết quả
- [ ] Cung cấp code đáp án mẫu (Java client) cho cả 9 bài tập
- [ ] Cung cấp script/lệnh để build và chạy server ngay lập tức
- [ ] Hướng dẫn triển khai trên DigitalOcean

### Out of Scope

- Biên dịch và chạy code Java của sinh viên trên server (sandbox) — chỉ so sánh kết quả output
- Real-time leaderboard hoặc ranking giữa các sinh viên
- OAuth / SSO — chỉ dùng local auth
- Bài tập ngoài 9 bài mẫu
- Database (PostgreSQL, MySQL, MongoDB...) — toàn bộ dùng JSON file

## Context

- Đây là hệ thống luyện tập cho sinh viên PTIT, môn lập trình mạng / distributed systems.
- 9 bài tập mẫu đã có sẵn trong thư mục `luyentap/` (1.md–9.md), bao gồm mã câu hỏi và yêu cầu chi tiết.
- Mỗi bài tập yêu cầu sinh viên viết client (Java) để gọi server, xử lý data, và submit kết quả.
- Server cần mô phỏng chính xác hành vi của exam server PTIT (36.50.135.242) để sinh viên có thể test trước khi thi thật.

## Constraints

- **Tech stack**: Java 17+, Spring Boot 3.x, Maven, gRPC, JAX-WS/Spring-WS — vì yêu cầu là server Java và cần hỗ trợ cả 3 giao thức
- **Storage**: JSON file trên disk — không dùng database theo yêu cầu
- **Triển khai**: DigitalOcean droplet (Ubuntu) — cần hướng dẫn deploy chi tiết
- **Thứ tự giao thức**: REST → gRPC → SOAP — vì SOAP phức tạp nhất, để cuối
- **Frontend**: Web UI đơn giản, có thể dùng Thymeleaf hoặc static HTML + JS — không cần SPA framework

## Key Decisions

| Decision | Rationale | Outcome |
|----------|-----------|---------|
| So sánh kết quả (không sandbox) | Đơn giản, an toàn, phù hợp với exam server PTIT hiện tại | — Pending |
| JSON file storage | Theo yêu cầu, không cần setup DB | — Pending |
| Spring Boot + Maven | Phổ biến, tài liệu nhiều, sinh viên dễ tiếp cận | — Pending |
| Thymeleaf cho Web UI | Tích hợp sẵn với Spring Boot, không cần build frontend riêng | — Pending |

## Evolution

This document evolves at phase transitions and milestone boundaries.

**After each phase transition** (via `/gsd-transition`):
1. Requirements invalidated? → Move to Out of Scope with reason
2. Requirements validated? → Move to Validated with phase reference
3. New requirements emerged? → Add to Active
4. Decisions to log? → Add to Key Decisions
5. "What This Is" still accurate? → Update if drifted

**After each milestone** (via `/gsd-complete-milestone`):
1. Full review of all sections
2. Core Value check — still the right priority?
3. Audit Out of Scope — reasons still valid?
4. Update Context with current state

---
*Last updated: 2025-05-09 after initialization*
