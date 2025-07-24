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
@Table(name = "seo_analyses")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"project", "user"})
public class SEOAnalysis extends Base{
	
    
    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

   
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Column(name = "ai_response", nullable = false)
    private String aiResponse;

    


}
