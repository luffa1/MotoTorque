package com.luffa.mototorque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "maintenance_spec")
public class MaintenanceSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Motorcycle motorcycle;

    @Column(nullable = false)
    private String category;            //Chain, Tires, Fluids

    @Column(nullable = false)
    private String parameter;           //"Chain slack"

    @Column(name = "spec_value", nullable = false)
    private String value;               //Chain slack value

    private String unit;

    private String conditions;          //np "Cold tires"
    private String notes;

    protected MaintenanceSpec(){

    }

    public MaintenanceSpec(Motorcycle motorcycle,
                         String category,
                         String parameter,
                         String value,
                         String unit,
                         String conditions,
                         String notes) {
        this.motorcycle = motorcycle;
        this.category = category;
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
        this.conditions = conditions;
        this.notes = notes;
    }

    public Motorcycle getMotorcycle() {
        return motorcycle;
    }

    public void setMotorcycle(Motorcycle motorcycle) {
        this.motorcycle = motorcycle;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
