package vn.ptit.judge.repository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public abstract class JsonFileRepository<T> {
    protected final ObjectMapper objectMapper;
    protected final File file;
    protected final Class<T> type;
    protected final ReentrantLock lock = new ReentrantLock();

    public JsonFileRepository(ObjectMapper objectMapper, String filePath, Class<T> type) {
        this.objectMapper = objectMapper;
        this.file = new File(filePath);
        this.type = type;
    }

    public List<T> findAll() {
        lock.lock();
        try {
            if (!file.exists() || file.length() == 0) {
                return new ArrayList<>();
            }
            JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        } finally {
            lock.unlock();
        }
    }

    public T save(T entity) {
        lock.lock();
        try {
            List<T> entities = findAll();
            entities.add(entity);
            writeAtomically(entities);
            return entity;
        } finally {
            lock.unlock();
        }
    }

    public void saveAll(List<T> entities) {
        lock.lock();
        try {
            writeAtomically(entities);
        } finally {
            lock.unlock();
        }
    }

    private void writeAtomically(List<T> entities) {
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            File tempFile = new File(file.getAbsolutePath() + ".tmp");
            objectMapper.writeValue(tempFile, entities);
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON file: " + file.getPath(), e);
        }
    }
}
