package com.cdac.entities;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "rankings")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"project"})
public class Ranking extends Base {
    
    @Column(name = "position", nullable = false)
    private Integer position;
    
    @Column(name = "date_checked", nullable = false)
    private LocalDate dateChecked;
    
    @Column(name = "keyword", length = 255)
    private String keyword;
    
    @Column(name = "search_engine", length = 50)
    private String searchEngine; // Google, Bing, etc.
    
    @Column(name = "location", length = 100)
    private String location; // Geographic location for search
    
    @Column(name = "url_found", length = 500)
    private String urlFound; // The actual URL that ranked
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}