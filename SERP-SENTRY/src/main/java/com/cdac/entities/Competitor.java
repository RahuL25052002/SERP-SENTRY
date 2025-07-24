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
@Table(name = "competitors") 
@NoArgsConstructor
@Getter
@Setter
@ToString( callSuper = true, exclude = "project")

public class Competitor extends Base{

    @Column(name = "competitor_domain", nullable = false, length = 30)
    private String competitorDomain;

    @Column(name = "notes",length = 1000)
    private String notes;
    
    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
