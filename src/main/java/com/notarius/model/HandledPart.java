package com.notarius.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class HandledPart
{

    public HandledPart() {

    }
    public HandledPart(String originalHandledPart) {
        this.originalHandledPart = originalHandledPart;
    }

    @Id
    @Column
    @SequenceGenerator(name="pk_seq",sequenceName="handled_part_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_seq")
    private Long id;
    @Column
    private String originalHandledPart;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(
            name = "URL_BRIDGE",
            joinColumns = @JoinColumn(name = "handled_part_id", updatable = false),
            inverseJoinColumns = @JoinColumn(name = "domain_part_id", updatable = false))
    private Set<DomainPart> domainParts = new HashSet<>();;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalHandledPart() {
        return originalHandledPart;
    }

    public void setOriginalHandledPart(String originalHandledPart) {
        this.originalHandledPart = originalHandledPart;
    }

    public Set<DomainPart> getDomainParts() {
        return domainParts;
    }

    public void setDomainParts(Set<DomainPart> domainParts) {
        this.domainParts = domainParts;
    }
}