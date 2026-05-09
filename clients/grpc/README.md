# gRPC Client

Môn: Hệ thống phân tán — PTIT

## Build

```bash
cd clients/grpc
mvn clean compile
```

## Run

```bash
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.grpc.Bai4Sum"
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.grpc.Bai5Sort"
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.grpc.Bai6Object"
```

Hoặc chạy trực tiếp sau khi compile:

```bash
java -cp target/classes:$(mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout) vn.ptit.judge.client.grpc.Bai4Sum
```

Trên Windows PowerShell:

```powershell
$cp = mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout
java -cp "target/classes;$cp" vn.ptit.judge.client.grpc.Bai4Sum
```

## Mô tả bài tập

| Class | qCode | Mô tả |
|-------|-------|-------|
| `Bai4Sum` | `kpFm4QvE` | Tính tổng dãy số nguyên (CSV) |
| `Bai5Sort` | `0VdN4dph` | Sắp xếp từ theo alphabet (CSV) |
| `Bai6Object` | `1zog4SNL` | Tính giá cuối sản phẩm (JSON) |

Server gRPC address: `localhost:2240`

Stub được generate từ `judge.proto` và copy sẵn vào `src/main/java/GRPC/` để tránh lỗi Unicode path trên Windows khi chạy `protoc`.
