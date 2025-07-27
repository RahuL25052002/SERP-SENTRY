package com.cdac.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SerpResultDTO {
    private Integer position;
    private String title;
    private String link;
    private String snippet;
    private String displayedLink;
}