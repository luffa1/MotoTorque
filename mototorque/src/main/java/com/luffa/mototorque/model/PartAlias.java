package com.luffa.mototorque.model;


import jakarta.persistence.*;

@Entity
@Table(name = "part_alias")
public class PartAlias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false)
    private String partName;

    public PartAlias() {
    }

    public PartAlias(String alias, String partName) {
        this.alias = alias;
        this.partName = partName;
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }
}
