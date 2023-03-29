package com.hub.gifticon.domain.gifticon;

import com.hub.gifticon.domain.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GifticonService {
    private final FileStore fileStore;
    private final GifticonRepository gifticonRepository;

    public Gifticon addGifticon(String gifticonName,
                                LocalDate expirationDate,
                                MultipartFile multipartFile) throws IOException {
        Gifticon gifticon = new Gifticon();
        gifticon.setGifticonName(gifticonName);
        gifticon.setExpirationDate(expirationDate);

        // 파일을 디스크에 저장
        String filename = fileStore.storeFile(multipartFile);
        gifticon.setImageFilename(filename);

        // 기프티콘을 데이터베이스에 저장
        return gifticonRepository.add(gifticon);
    }

    public Gifticon editGifticon(Long gifticonId,
                             String gifticonName,
                             LocalDate expirationDate,
                             MultipartFile multipartFile) throws IOException {
        Gifticon outdatedGifticon = gifticonRepository.findById(gifticonId);
        if (outdatedGifticon == null) {
            return null;
        }

        // 기존 이미지를 디스크에서 삭제
        fileStore.deleteFile(outdatedGifticon.getImageFilename());

        // 새 이미지를 디스크에 저장
        String filename = fileStore.storeFile(multipartFile);

        Gifticon updatedGifticon = new Gifticon();
        updatedGifticon.setGifticonId(gifticonId);
        updatedGifticon.setGifticonName(gifticonName);
        updatedGifticon.setExpirationDate(expirationDate);
        updatedGifticon.setImageFilename(filename);

        // 데이터베이스 업데이트
        gifticonRepository.update(gifticonId, updatedGifticon);

        return updatedGifticon;
    }

    public void deleteGifticon(Long gifticonId) {
        Gifticon gifticon = gifticonRepository.findById(gifticonId);
        fileStore.deleteFile(gifticon.getImageFilename());
        gifticonRepository.delete(gifticonId);
    }

    public void useGifticon(Long gifticonId) {
        gifticonRepository.use(gifticonId);
    }
}
