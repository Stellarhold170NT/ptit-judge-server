<!-- GSD:project-start source:PROJECT.md -->
## Project

**Java Judge Server — PTIT Luyện Tập**

Một hệ thống chấm điểm (judge server) viết bằng Java, triển khai trên DigitalOcean, dành cho sinh viên PTIT luyện tập 9 bài tập mẫu phân bổ trên 3 giao thức: REST, gRPC, và SOAP. Server cung cấp endpoint cho từng bài tập, nhận kết quả từ sinh viên, so sánh với đáp án, và trả về trạng thái AC/WA/RTE. Hệ thống bao gồm web UI đơn giản để quản lý tài khoản, submit bài, và xem kết quả. Không dùng database — toàn bộ dữ liệu lưu bằng JSON file.

**Core Value:** Sinh viên có thể luyện tập 9 bài tập mẫu (REST/gRPC/SOAP) trên một server local/deploy được, với đầy đủ code đáp án mẫu và lệnh chạy ngay.

### Constraints

- **Tech stack**: Java 17+, Spring Boot 3.x, Maven, gRPC, JAX-WS/Spring-WS — vì yêu cầu là server Java và cần hỗ trợ cả 3 giao thức
- **Storage**: JSON file trên disk — không dùng database theo yêu cầu
- **Triển khai**: DigitalOcean droplet (Ubuntu) — cần hướng dẫn deploy chi tiết
- **Thứ tự giao thức**: REST → gRPC → SOAP — vì SOAP phức tạp nhất, để cuối
- **Frontend**: Web UI đơn giản, có thể dùng Thymeleaf hoặc static HTML + JS — không cần SPA framework
<!-- GSD:project-end -->

<!-- GSD:stack-start source:STACK.md -->
## Technology Stack

Technology stack not yet documented. Will populate after codebase mapping or first phase.
<!-- GSD:stack-end -->

<!-- GSD:conventions-start source:CONVENTIONS.md -->
## Conventions

Conventions not yet established. Will populate as patterns emerge during development.
<!-- GSD:conventions-end -->

<!-- GSD:architecture-start source:ARCHITECTURE.md -->
## Architecture

Architecture not yet mapped. Follow existing patterns found in the codebase.
<!-- GSD:architecture-end -->

<!-- GSD:skills-start source:skills/ -->
## Project Skills

No project skills found. Add skills to any of: `.claude/skills/`, `.agents/skills/`, `.cursor/skills/`, `.github/skills/`, or `.codex/skills/` with a `SKILL.md` index file.
<!-- GSD:skills-end -->

<!-- GSD:workflow-start source:GSD defaults -->
## GSD Workflow Enforcement

Before using Edit, Write, or other file-changing tools, start work through a GSD command so planning artifacts and execution context stay in sync.

Use these entry points:
- `/gsd-quick` for small fixes, doc updates, and ad-hoc tasks
- `/gsd-debug` for investigation and bug fixing
- `/gsd-execute-phase` for planned phase work

Do not make direct repo edits outside a GSD workflow unless the user explicitly asks to bypass it.
<!-- GSD:workflow-end -->



<!-- GSD:profile-start -->
## Developer Profile

> Profile not yet configured. Run `/gsd-profile-user` to generate your developer profile.
> This section is managed by `generate-claude-profile` -- do not edit manually.
<!-- GSD:profile-end -->
