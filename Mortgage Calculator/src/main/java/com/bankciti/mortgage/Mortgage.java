package com.bankciti.mortgage;

import java.math.BigDecimal;

public class Mortgage {

    private static final int MONTHS_IN_YEAR = 12;
    private BigDecimal interestRate;
    private int loanTerm;
    private String startMonth;
    private int startMonthNum;
    private int startYear;
    private BigDecimal pmiRate;
    private BigDecimal startingPrincipal;
    private BigDecimal monthlyMortgage;
    private BigDecimal principalBalance;
    private BigDecimal principalPayment;
    private BigDecimal amortizingInterestAmount;
    private BigDecimal totalInterest;

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public int getMonthsToRepay() {
        return loanTerm * MONTHS_IN_YEAR;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public int getStartMonthNum() {
        return startMonthNum;
    }

    public void setStartMonthNum(int startMonthNum) {
        this.startMonthNum = startMonthNum;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public BigDecimal getPmiRate() {
        return pmiRate;
    }

    public void setPmiRate(BigDecimal pmiRate) {
        this.pmiRate = pmiRate;
    }

    public BigDecimal getStartingPrincipal() {
        return startingPrincipal;
    }

    public void setStartingPrincipal(BigDecimal startingPrincipal) {
        this.startingPrincipal = startingPrincipal;
    }

    public void setMonthlyMortgage(BigDecimal monthlyMortgage) {
        this.monthlyMortgage = monthlyMortgage;
    }

    public BigDecimal getMonthlyMortgage() {
        return monthlyMortgage;
    }

    public int getTotalPayments() {
        return MONTHS_IN_YEAR * loanTerm;
    }

    public void setPrincipalBalance(BigDecimal principalBalance) {
        this.principalBalance = principalBalance;
    }

    public BigDecimal getPrincipalBalance() {
        return principalBalance;
    }

    public void setPrincipalPayment(BigDecimal principalPayment) {
        this.principalPayment = principalPayment;
    }

    public BigDecimal getPrincipalPayment() {
        return principalPayment;
    }

    public BigDecimal getAmortizingInterestAmount() {
        return amortizingInterestAmount;
    }

    public void setAmortizingInterestAmount(BigDecimal amortizingInterest) {
        this.amortizingInterestAmount = amortizingInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

}
