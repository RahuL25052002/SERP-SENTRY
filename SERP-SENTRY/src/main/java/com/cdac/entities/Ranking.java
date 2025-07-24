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
@Table(name = "rankings") 
@NoArgsConstructor
@Getter
@Setter
@ToString( callSuper = true, exclude = {"keyword", "project"})
public class Ranking extends Base{
	
	@ManyToOne()
    @JoinColumn(name = "keyword_id", nullable = false)
    private Keyword keyword;

    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "search_engine", nullable = false)
    private SearchEngine searchEngine;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "date_checked", nullable = false)
    private LocalDate dateChecked;

}
