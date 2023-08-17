package com.notarius.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class DomainPart {

    public DomainPart() {

    }

    public DomainPart(String fixedUrl) {
        this.fixedUrl = fixedUrl;
    }

    @Id
    @Column
    //@SequenceGenerator(name="pk_seq",sequenceName="domain_part_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column
    private String fixedUrl;

    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy = "domainParts")
    private Set<HandledPart> handledParts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFixedUrl() {
        return fixedUrl;
    }

    public void setFixedUrl(String fixedUrl) {
        this.fixedUrl = fixedUrl;
    }

    public Set<HandledPart> getHandledParts() {
        return handledParts;
    }

    public void setHandledParts(Set<HandledPart> handledParts) {
        this.handledParts = handledParts;
    }
}