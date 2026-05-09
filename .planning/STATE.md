# Project State: Java Judge Server

**Project:** Java Judge Server — PTIT Luyện Tập  
**Current Phase:** Not started  
**Last Updated:** 2025-05-09  

## Project Reference

See: `.planning/PROJECT.md` (updated 2025-05-09)

**Core value:** Sinh viên có thể luyện tập 9 bài tập mẫu (REST/gRPC/SOAP) trên một server local/deploy được, với đầy đủ code đáp án mẫu và lệnh chạy ngay.
**Current focus:** Phase 1 — Project Setup & Auth Foundation

## Phase Status

| Phase | Name | Status | Requirements | Blocked By |
|-------|------|--------|--------------|------------|
| 1 | Project Setup & Auth Foundation | ⏳ Pending | 11 | — |
| 2 | REST Services — Bài 1–3 | ⏳ Pending | 7 | Phase 1 |
| 3 | gRPC Services — Bài 4–6 | ⏳ Pending | 7 | Phase 2 |
| 4 | SOAP Services — Bài 7–9 | ⏳ Pending | 6 | Phase 3 |
| 5 | Web UI & Dashboard | ⏳ Pending | 4 | Phase 4 |
| 6 | Answer Code, Docker & Deployment | ⏳ Pending | 3 | Phase 5 |

## Active Decisions

| ID | Decision | Status | Context |
|----|----------|--------|---------|
| D-01 | So sánh kết quả (không sandbox) | ✅ Locked | Yêu cầu user, phù hợp exam PTIT |
| D-02 | JSON file storage | ✅ Locked | Yêu cầu user |
| D-03 | Spring Boot + Maven | ✅ Locked | Phổ biến, dễ deploy |
| D-04 | Thymeleaf cho Web UI | ✅ Locked | Tích hợp Spring Boot |

## Blockers

None.

## Notes

- 9 bài tập mẫu đã có sẵn trong `luyentap/1.md`–`9.md`
- Server deploy trên DigitalOcean
- Mục tiêu MVP: sinh viên có thể đăng ký, đăng nhập, và luyện tập cả 9 bài

---
*State updated: 2025-05-09 after initialization*
