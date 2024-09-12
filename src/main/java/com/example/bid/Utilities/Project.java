package com.example.bid.Utilities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private LocalDateTime bidingDDL;
    private BigDecimal curLowestPrice;
    private String winningBuyer;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Bid> bids;

    public Project() {
    }

    public Project(String name, LocalDateTime bidingDDL) {
        this.name = name;
        this.bidingDDL = bidingDDL;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBidingDDL() {
        return bidingDDL;
    }

    public void setBidingDDL(LocalDateTime bidingDDL) {
        this.bidingDDL = bidingDDL;
    }

    public BigDecimal getCurLowestPrice() {
        return curLowestPrice;
    }

    public void setCurLowestPrice(BigDecimal curLowestPrice) {
        this.curLowestPrice = curLowestPrice;
    }

    public String getWinningBuyer() {
        return winningBuyer;
    }

    public void setWinningBuyer(String winningBuyer) {
        this.winningBuyer = winningBuyer;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
}
