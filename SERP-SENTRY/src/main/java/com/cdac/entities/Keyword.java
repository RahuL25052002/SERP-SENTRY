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
@Table(name = "keywords")
@NoArgsConstructor
@Getter
@Setter
@ToString( callSuper = true, exclude = {"rankings"})
public class Keyword extends Base{
	
	 @Column(name = "keyword_text", nullable = false)
	    private String keywordText;

	    @ManyToOne()
	    @JoinColumn(name = "project_id", nullable = false)
	    private Project project;


	    @OneToMany(mappedBy = "keyword", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<Ranking> rankings = new ArrayList<>();

}
