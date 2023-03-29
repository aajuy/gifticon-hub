package com.hub.gifticon.domain.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${file.dir}")
    private String fileDir;

    // MultipartFile을 File로 변환하여 저장
    // 저장된 파일의 이름을 반환한다.
    public String storeFile(MultipartFile multipartFile) throws IOException {
        String filename = createFilenameFrom(multipartFile.getOriginalFilename());
        String fullPath = getFullPath(filename);
        multipartFile.transferTo(new File(fullPath));
        return filename;
    }

    public void deleteFile(String filename) {
        String fullPath = getFullPath(filename);
        File file = new File(fullPath);
        file.delete();
        return;
    }

    // 디스크에 저장될 파일 이름을 반환한다.
    private String createFilenameFrom(String originalFilename) {
        return UUID.randomUUID().toString() + originalFilename;
    }

    public String getFullPath(String filename) {
        return fileDir + filename;
    }
}
