package com.hub.gifticon.domain.gifticon;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class Gifticon {
    private Long gifticonId;
    private String gifticonName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    private Boolean isUsed;
    private String imageFilename;
}
