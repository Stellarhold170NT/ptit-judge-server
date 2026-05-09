# REST Client

Môn: Hệ thống phân tán — PTIT

## Build

```bash
cd clients/rest
mvn clean compile
```

## Run

```bash
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.rest.Bai1Sum"
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.rest.Bai2Sort"
mvn exec:java -Dexec.mainClass="vn.ptit.judge.client.rest.Bai3Object"
```

Hoặc chạy trực tiếp sau khi compile:

```bash
java -cp target/classes:$(mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout) vn.ptit.judge.client.rest.Bai1Sum
```

Trên Windows PowerShell:

```powershell
$cp = mvn dependency:build-classpath -q -DincludeScope=compile -Dmdep.outputFile=/dev/stdout
java -cp "target/classes;$cp" vn.ptit.judge.client.rest.Bai1Sum
```

## Mô tả bài tập

| Class | qCode | Mô tả |
|-------|-------|-------|
| `Bai1Sum` | `4siaIVgn` | Tính tổng dãy số nguyên |
| `Bai2Sort` | `oPW4mpqm` | Sắp xếp từ theo alphabet |
| `Bai3Object` | `2ucfcULf` | Tính giá cuối sản phẩm |

Server base URL: `http://localhost:2230`
