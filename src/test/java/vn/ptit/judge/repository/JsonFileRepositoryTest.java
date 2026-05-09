package vn.ptit.judge.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class JsonFileRepositoryTest {

    private ObjectMapper objectMapper;
    private Path tempDir;
    private TestRepository repository;

    static class TestEntity {
        private String id;
        private String value;

        public TestEntity() {
        }

        public TestEntity(String id, String value) {
            this.id = id;
            this.value = value;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class TestRepository extends JsonFileRepository<TestEntity> {
        public TestRepository(ObjectMapper objectMapper, String filePath) {
            super(objectMapper, filePath, TestEntity.class);
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        tempDir = Files.createTempDirectory("judge-test-");
        repository = new TestRepository(objectMapper, tempDir.resolve("test.json").toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (Exception ignored) {
                    }
                });
    }

    @Test
    void findAll_NonExistentFile_ReturnsEmptyList() {
        List<TestEntity> result = repository.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_EmptyFile_ReturnsEmptyList() throws Exception {
        Files.createFile(tempDir.resolve("test.json"));
        List<TestEntity> result = repository.findAll();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_AppendsCorrectly() {
        repository.save(new TestEntity("1", "first"));
        repository.save(new TestEntity("2", "second"));

        List<TestEntity> all = repository.findAll();
        assertEquals(2, all.size());
        assertEquals("first", all.get(0).getValue());
        assertEquals("second", all.get(1).getValue());
    }

    @Test
    void saveAll_ReplacesCorrectly() {
        repository.save(new TestEntity("old", "old-val"));
        assertEquals(1, repository.findAll().size());

        List<TestEntity> newList = List.of(
                new TestEntity("a", "new1"),
                new TestEntity("b", "new2")
        );
        repository.saveAll(newList);

        List<TestEntity> all = repository.findAll();
        assertEquals(2, all.size());
        assertEquals("new1", all.get(0).getValue());
        assertEquals("new2", all.get(1).getValue());
    }

    @Test
    void threadSafety_MultipleThreadsSaving() throws Exception {
        int threadCount = 2;
        int itemsPerThread = 50;
        CountDownLatch ready = new CountDownLatch(threadCount);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threadCount);
        AtomicInteger errors = new AtomicInteger(0);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            new Thread(() -> {
                ready.countDown();
                try {
                    start.await();
                    for (int i = 0; i < itemsPerThread; i++) {
                        repository.save(new TestEntity("t" + threadId + "-" + i, "val"));
                    }
                } catch (Exception e) {
                    errors.incrementAndGet();
                } finally {
                    done.countDown();
                }
            }).start();
        }

        ready.await();
        start.countDown();
        done.await();

        assertEquals(0, errors.get());
        List<TestEntity> all = repository.findAll();
        assertEquals(threadCount * itemsPerThread, all.size());
    }
}
