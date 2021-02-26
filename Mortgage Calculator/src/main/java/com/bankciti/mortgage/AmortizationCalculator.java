package com.bankciti.mortgage;

import java.math.BigDecimal;

public class AmortizationCalculator extends MortgageCalculator {

    private StringBuilder schedule = new StringBuilder();
    private int yearCounter = firstMortgage.getStartYear();

    public AmortizationCalculator(Mortgage firstMortgage, House firstHome) {
        super(firstMortgage, firstHome);
    }

    public String calcAmortization() {
        setValues();
        displayHeaders();
        displayFirstYear();
        displayRemainderTerm();
        return convertToString(schedule);
    }

    private void setValues() {
        firstMortgage.setPrincipalBalance(firstMortgage.getStartingPrincipal());
        firstMortgage.setTotalInterest(setNewBigDecimal());
        firstMortgage.setPrincipalPayment(setNewBigDecimal());
    }

    private void displayHeaders() {
        schedule.append("Payment Date").append("           ").append("Payment").append("          ");
        schedule.append("Principal").append("      ").append("Interest").append("           ");
        schedule.append("Total Interest").append("                    ").append("Balance").append(System.lineSeparator());
    }

    private void displayFirstYear() {
        for (int i = firstMortgage.getStartMonthNum(); i <= MONTHS_IN_YEAR; i++) {

            calcAndDisplay(yearCounter, i);
        }
    }

    private void displayRemainderTerm() {
        for (int j = 0; j < firstMortgage.getLoanTerm() - 1; j++) {
            yearCounter++;

            for (int l = 1; l <= MONTHS_IN_YEAR; l++) {
                calcAndDisplay(yearCounter, l);
            }
        }

        //get the last year
        yearCounter++;

        for (int k = 1; k < firstMortgage.getStartMonthNum(); k++) {
            calcAndDisplay(yearCounter, k);
        }
    }

    private String convertToString(StringBuilder amortSchedule) {
        return amortSchedule.toString();
    }

    private void calcAndDisplay(int yearCounter, int i) {
        calcAmortizingInterestAmount();
        calcTotalInterest();

        firstMortgage.setPrincipalPayment(calcMonthlyPrincipalTowardsBalance());
        firstMortgage.setPrincipalBalance(firstMortgage.getPrincipalBalance().subtract(calcMonthlyPrincipalTowardsBalance()));

        display(i);
    }

    private void calcAmortizingInterestAmount() {
        BigDecimal yearlyInterest = firstMortgage.getPrincipalBalance().multiply(firstMortgage.getInterestRate());
        firstMortgage.setAmortizingInterestAmount(yearlyInterest.divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc));
    }

    private void calcTotalInterest() {
        firstMortgage.setTotalInterest(firstMortgage.getTotalInterest().add(firstMortgage.getAmortizingInterestAmount()));
    }

    private void display(int i) {
        //payment date
        schedule.append(months.get(i)).append(" ").append(yearCounter + firstMortgage.getStartYear()).append("               ");
        //payment
        schedule.append("$").append(monthlyMortgage()).append("        ");
        //principal
        schedule.append("$").append(df.format(calcMonthlyPrincipalTowardsBalance())).append("        ");
        //interest
        schedule.append("$").append(df.format(firstMortgage.getAmortizingInterestAmount())).append("            ");
        //total interest
        schedule.append("$").append(df.format(firstMortgage.getTotalInterest())).append("                     ");
        //balance
        schedule.append("$").append(df.format(firstMortgage.getPrincipalBalance().abs())).append(System.lineSeparator());
    }

    private BigDecimal calcMonthlyPrincipalTowardsBalance() {
        BigDecimal notTowardBalance = monthlyPropTax().add(monthlyHomeIns()).add(monthlyHoa()).add(monthlyPMIAmount()).add(firstMortgage.getAmortizingInterestAmount());
        return firstMortgage.getMonthlyMortgage().subtract(notTowardBalance);
    }

}



