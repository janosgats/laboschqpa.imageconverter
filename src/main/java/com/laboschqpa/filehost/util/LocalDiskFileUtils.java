package com.laboschqpa.filehost.util;

import com.laboschqpa.filehost.entity.LocalDiskFileEntity;
import com.laboschqpa.filehost.enums.apierrordescriptor.FileServingApiError;
import com.laboschqpa.filehost.exceptions.apierrordescriptor.FileServingException;
import com.laboschqpa.filehost.repo.LocalDiskFileEntityRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class LocalDiskFileUtils {
    private static final String ACTIVE_MOUNT_NAME = "mnt1";

    @Value("${filehost.localdiskfile.basepath}")
    private String localDiskFileBasePath;

    private final LocalDiskFileEntityRepository localDiskFileEntityRepository;

    public String getFullPathFromStoredFileEntityPath(String localDiskFileEntityPath) {
        if (StringUtils.isBlank(localDiskFileEntityPath)) {
            throw new FileServingException(FileServingApiError.INVALID_STORED_FILE, "localDiskFileEntityPath is blank!");
        }
        return Path.of(localDiskFileBasePath, localDiskFileEntityPath).toString();
    }

    public String generateNewLocalDiskFileEntityPath(Long localDiskFileEntityId) {
        Objects.requireNonNull(localDiskFileEntityId, "localDiskFileEntity cannot be null when generation new file path!");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
        return Path.of(
                ACTIVE_MOUNT_NAME,
                String.valueOf(zonedDateTime.getYear()),
                String.valueOf(zonedDateTime.getMonthValue()),
                String.valueOf(zonedDateTime.getDayOfMonth()),
                "f_" + localDiskFileEntityId + ".sf"
        ).toString();
    }

    public LocalDiskFileEntity saveLocalDiskFileEntity(LocalDiskFileEntity localDiskFileEntity) {
        return localDiskFileEntityRepository.save(localDiskFileEntity);
    }
}
