# Java Judge Server — PTIT Luyện Tập

Server chấm điểm 9 bài tập mẫu (REST/gRPC/SOAP) cho sinh viên PTIT. Không dùng database, toàn bộ dữ liệu được lưu bằng JSON file.

## Tech Stack

- **Java 17**
- **Spring Boot 3.2**
- **Maven**
- **Thymeleaf** — Web UI
- **gRPC** — High-performance RPC
- **Spring WS** — SOAP web services
- **Jackson** — JSON processing

## Features

- **3 bài REST**:
  - Tính tổng dãy số nguyên (CSV)
  - Sắp xếp từ theo alphabet
  - Tính giá cuối sản phẩm

- **3 bài gRPC**:
  - Tính tổng dãy số nguyên (CSV)
  - Sắp xếp từ theo alphabet
  - Tính giá cuối sản phẩm

- **3 bài SOAP**:
  - Tính tổng list int
  - Đảo ngược chuỗi
  - Tính giá cuối sản phẩm

- **Authentication**: Đăng ký, đăng nhập, đăng xuất (token-based)
- **Web UI**: Dashboard, trang bài tập, lịch sử submit
- **JSON file storage** (users, submissions, sessions) trong thư mục `./data/`

## Build & Run

```bash
mvn clean package
java -jar target/judge-server-1.0.0.jar
```

Server sẽ khởi động tại:
- REST / Web UI: `http://localhost:2230`
- gRPC: `localhost:2240`

## Docker

### Chạy từ source (build local)

```bash
docker-compose up --build
```

### Chạy từ Docker image có sẵn (không cần build)

Nếu bạn có file `ptit-judge-server.tar` (image đã build sẵn):

```bash
# 1. Load image
docker load -i ptit-judge-server.tar

# 2. Chạy container
docker run -d \
  -p 2230:2230 \
  -p 2240:2240 \
  -v $(pwd)/data:/app/data \
  --name judge-server \
  ptit-judge-server:latest
```

Hoặc nếu đã pull từ Docker Hub:

```bash
docker run -d -p 2230:2230 -p 2240:2240 --name judge-server stellarhold170nt/ptit-judge-server:latest
```

Sau khi chạy, truy cập:
- Web UI: `http://localhost:2230`
- SOAP WSDL: `http://localhost:2230/ws/DataService.wsdl`
- gRPC: `localhost:2240`

## API Overview

### REST

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| `GET` | `/api/data/{qCode}` | Lấy dữ liệu đầu vào của bài tập |
| `POST` | `/api/submit` | Nộp đáp án và nhận kết quả chấm điểm |

Tham số:
- `studentCode` — mã sinh viên
- `qCode` — mã bài tập

### gRPC

- **Port**: `2240`
- **Proto file**: `src/main/proto/judge.proto`
- **Service**: `JudgeService`
- **Methods**:
  - `Request(JudgeRequest) → JudgeResponse` — Lấy dữ liệu đầu vào
  - `Submit(SubmitRequest) → SubmitResponse` — Nộp đáp án

### SOAP

- **WSDL DataService**: `http://localhost:2230/ws/DataService.wsdl`
- **WSDL CharacterService**: `http://localhost:2230/ws/CharacterService.wsdl`
- **WSDL ObjectService**: `http://localhost:2230/ws/ObjectService.wsdl`

## Test

```bash
mvn test
```

## Client Code

Thư mục `clients/` chứa code đáp án mẫu cho sinh viên tham khảo (Java client cho REST, gRPC, SOAP).

## Deployment

Xem hướng dẫn triển khai chi tiết tại [DEPLOY.md](DEPLOY.md).

## Danh sách bài tập (qCode)

| qCode | Tên bài | Giao thức |
|-------|---------|-----------|
| `4siaIVgn` | Tính tổng dãy số nguyên | REST |
| `oPW4mpqm` | Sắp xếp từ alphabet | REST |
| `2ucfcULf` | Tính giá cuối sản phẩm | REST |
| `kpFm4QvE` | Tính tổng | gRPC |
| `0VdN4dph` | Sắp xếp từ | gRPC |
| `1zog4SNL` | Giá cuối | gRPC |
| `yyhHZUpt` | Tính tổng | SOAP |
| `qvCGMEhW` | Đảo ngược chuỗi | SOAP |
| `7u07dNqg` | Giá cuối | SOAP |

## Dữ liệu

Tất cả dữ liệu runtime được lưu trong thư mục `./data/` dưới dạng JSON file (không dùng database).

## Giấy phép

Dự án phục vụ mục đích học tập tại PTIT.
