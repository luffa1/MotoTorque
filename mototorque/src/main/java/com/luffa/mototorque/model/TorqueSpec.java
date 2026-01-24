package com.luffa.mototorque.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
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

}
