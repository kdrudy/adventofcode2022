package com.kyrutech.adventofcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Monkey {
    private Integer id;
    private List<Long> items = new ArrayList<>();
    private String operation = "";
    private Integer test;
    private Integer ifTrue;
    private Integer ifFalse;

    private Long inspections = 0L;

    public Monkey(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public List<Long> addItem(Long item) {
        items.add(item);
        return items;
    }

    public List<Long> removeItem(Long item) {
        items.remove(item);
        return items;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public Integer getIfTrue() {
        return ifTrue;
    }

    public void setIfTrue(Integer ifTrue) {
        this.ifTrue = ifTrue;
    }

    public Integer getIfFalse() {
        return ifFalse;
    }

    public void setIfFalse(Integer ifFalse) {
        this.ifFalse = ifFalse;
    }

    public Long getInspections() {
        return inspections;
    }

    public void setInspections(Long inspections) {
        this.inspections = inspections;
    }

    public void incrementInspections() {
        inspections++;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "id=" + id +
                ", items=" + items +
                ", operation='" + operation + '\'' +
                ", test=" + test +
                ", ifTrue=" + ifTrue +
                ", ifFalse=" + ifFalse +
                ", inspections=" + inspections +
                '}';
    }
}
