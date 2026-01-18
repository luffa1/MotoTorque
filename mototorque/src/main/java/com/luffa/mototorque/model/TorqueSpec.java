package com.luffa.mototorque.model;

import jakarta.persistence.*;

@Entity
@Table(name = "torque_spec")
public class TorqueSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Motorcycle motorcycle;

    @Column(nullable = false)
    private String partName;

    @Column(nullable = false)
    private int torqueNm;

    @Column(length = 500)
    private String notes;

    @Column(nullable = false)
    private String source;

    private Integer torqueMinNm;
    private Integer torqueMaxNm;
    private Integer angleDegrees;

    private String threadSize;
    private String tool;
    private String lockingCompound;
    private String manualSection;

    private Integer manualPage;

    protected TorqueSpec(){
    }

    public TorqueSpec(Motorcycle motorcycle,
                      String partName,
                      int torqueNm,
                      String notes,
                      String source,
                      Integer torqueMinNm,
                      Integer torqueMaxNm,
                      Integer angleDegrees,
                      String threadSize,
                      String tool,
                      String lockingCompound,
                      String manualSection,
                      Integer manualPage) {
        this.motorcycle = motorcycle;
        this.partName = partName;
        this.torqueNm = torqueNm;
        this.notes = notes;
        this.source = source;
        this.torqueMinNm = torqueMinNm;
        this.torqueMaxNm = torqueMaxNm;
        this.angleDegrees = angleDegrees;
        this.threadSize = threadSize;
        this.tool = tool;
        this.lockingCompound = lockingCompound;
        this.manualSection = manualSection;
        this.manualPage = manualPage;
    }

    public Long getId() {
        return id;
    }

    public Motorcycle getMotorcycle() {
        return motorcycle;
    }

    public String getPartName() {
        return partName;
    }

    public int getTorqueNm() {
        return torqueNm;
    }

    public String getNotes() {
        return notes;
    }

    public String getSource() {
        return source;
    }

    public Integer getTorqueMinNm() {
        return torqueMinNm;
    }

    public Integer getTorqueMaxNm() {
        return torqueMaxNm;
    }

    public Integer getAngleDegrees() {
        return angleDegrees;
    }

    public String getThreadSize() {
        return threadSize;
    }

    public String getTool() {
        return tool;
    }

    public String getLockingCompound() {
        return lockingCompound;
    }

    public String getManualSection() {
        return manualSection;
    }

    public Integer getManualPage() {
        return manualPage;
    }
}
