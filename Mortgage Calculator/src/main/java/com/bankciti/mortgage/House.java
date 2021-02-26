package com.bankciti.mortgage;

import java.math.BigDecimal;

public class House {
    private BigDecimal homeValue;
    private BigDecimal downPayment;
    private BigDecimal annualPropTax;
    private BigDecimal annualHoa;
    private BigDecimal homeIns;

    public BigDecimal getHomeValue() {
        return homeValue;
    }

    public void setHomeValue(BigDecimal homeValue) {
        this.homeValue = homeValue;
    }

    public BigDecimal getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(BigDecimal downPayment) {
        this.downPayment = downPayment;
    }

    public BigDecimal getAnnualPropTax() { return annualPropTax; }

    public void setAnnualPropTax(BigDecimal annualPropTax) { this.annualPropTax = annualPropTax; }

    public BigDecimal getAnnualHoa() { return annualHoa; }

    public void setAnnualHoa(BigDecimal annualHoa) { this.annualHoa = annualHoa; }

    public BigDecimal getHomeIns() {
        return homeIns;
    }

    public void setHomeIns(BigDecimal homeIns) {
        this.homeIns = homeIns;
    }

}
