package com.cdac.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
@ToString( callSuper = true, exclude = {"keywords", "competitors", "rankings", "seoAnalyses"})
public class Project extends Base{
	
	@Column(name = "name", nullable = false)
    private String name;

    @Column(name = "domain_url", nullable = false)
    private String domainUrl;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keyword> keywords  = new ArrayList<>();;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Competitor> competitors = new ArrayList<>();;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ranking> rankings = new ArrayList<>();;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SEOAnalysis> seoAnalyses = new ArrayList<>();;

}
