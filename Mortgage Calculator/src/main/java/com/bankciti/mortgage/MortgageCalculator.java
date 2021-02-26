package com.bankciti.mortgage;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MortgageCalculator {

    protected static final int MONTHS_IN_YEAR = 12;
    protected static final MathContext mc = new MathContext(12, RoundingMode.HALF_EVEN);
    private House firstHome;
    protected Mortgage firstMortgage;
    protected static final DecimalFormat df = new DecimalFormat("###,##0.00");
    public Map<String, String> mortgageRepaymentSummary = new LinkedHashMap<>();
    public Map<Integer, String> months = new HashMap<>();


    public MortgageCalculator(Mortgage firstMortgage, House firstHome) {
        this.firstMortgage = firstMortgage;
        this.firstHome = firstHome;
        populateMonthNumToName();
    }

    protected BigDecimal setNewBigDecimal() {
        return new BigDecimal("0.00");
    }

    protected BigDecimal monthlyHoa() {
        return firstHome.getAnnualHoa().divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
    }

    private BigDecimal monthlyInterestRate() {
        return firstMortgage.getInterestRate().divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
    }

    private int monthsToRepay() {
        return firstMortgage.getMonthsToRepay();
    }

    private BigDecimal principal() {
        return firstHome.getHomeValue().subtract(firstHome.getDownPayment());
    }

    protected BigDecimal monthlyPropTax() {
        return firstHome.getAnnualPropTax().divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
    }

    protected BigDecimal monthlyHomeIns() {
        return firstHome.getHomeIns().divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
    }

    protected BigDecimal monthlyPMIAmount() {
        BigDecimal annualPmi = firstMortgage.getPmiRate().multiply(principal(), mc);
        BigDecimal monthlyPmi = annualPmi.divide(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
        return monthlyPmi;
    }

    public String monthlyMortgage() {
        // 1) (1 + r)^n
        BigDecimal one = new BigDecimal("1.00");
        BigDecimal xFactor = (monthlyInterestRate().add(one)).pow(monthsToRepay(), mc);
        // 2) r(1+r)^n
        BigDecimal dividend = monthlyInterestRate().multiply((xFactor), mc);
        // 3) (1+r)^n - 1
        BigDecimal divisor = xFactor.subtract(one);
        // 4) Divide
        BigDecimal quotient = dividend.divide((divisor), mc);
        // 5) Multiply by principal
        BigDecimal monthlyMortgage = principal().multiply((quotient), mc);
        //Add the optional add-ons:
        monthlyMortgage = monthlyMortgage.add(monthlyHomeIns()).add(monthlyPropTax()).add(monthlyHoa()).add(monthlyPMIAmount());
        firstMortgage.setMonthlyMortgage(monthlyMortgage);
        return df.format(firstMortgage.getMonthlyMortgage());
    }

    private String downPaymentPercent() {
        BigDecimal percent = firstHome.getDownPayment().divide(firstHome.getHomeValue(), mc);
        return df.format(percent.movePointRight(2));
    }

    private String payOffDate() {
        int yearLoanPaid = firstMortgage.getStartYear() + firstMortgage.getLoanTerm();
        return months.get(firstMortgage.getStartMonthNum() - 1) + " " + yearLoanPaid;
    }

    private String annualMortgagePayment() {
        BigDecimal annualMortgage = firstMortgage.getMonthlyMortgage().multiply(BigDecimal.valueOf(MONTHS_IN_YEAR), mc);
        return df.format(annualMortgage);
    }

    private String totalPropTax() {
        BigDecimal totalPropTax = firstHome.getAnnualPropTax().multiply(BigDecimal.valueOf(firstMortgage.getLoanTerm()), mc);
        return df.format(totalPropTax);
    }

    private String totalHomeIns() {
        BigDecimal totalHomeIns = firstHome.getHomeIns().multiply(BigDecimal.valueOf(firstMortgage.getLoanTerm()), mc);
        return df.format(totalHomeIns);
    }

    private String totalMortgage() {
        BigDecimal totalMortgage = firstMortgage.getMonthlyMortgage().multiply(BigDecimal.valueOf(firstMortgage.getTotalPayments()), mc);
        return df.format(totalMortgage);
    }

    private void populateMonthNumToName() {
        months.put(1, "Jan");
        months.put(2, "Feb");
        months.put(3, "Mar");
        months.put(4, "Apr");
        months.put(5, "May");
        months.put(6, "Jun");
        months.put(7, "Jul");
        months.put(8, "Aug");
        months.put(9, "Sep");
        months.put(10, "Oct");
        months.put(11, "Nov");
        months.put(12, "Dec");
    }

    public void repaymentSummary() {
        mortgageRepaymentSummary.put("Total Monthly Payment", "$" + monthlyMortgage());
        mortgageRepaymentSummary.put("PMI Rate", firstMortgage.getPmiRate().toString() + "%");
        mortgageRepaymentSummary.put("Down Payment Amount", "$" + df.format(firstHome.getDownPayment()));
        mortgageRepaymentSummary.put("Down Payment Percentage", downPaymentPercent() + "%");
        mortgageRepaymentSummary.put("Expected Loan Paid-Off Date", payOffDate());
        mortgageRepaymentSummary.put("Monthly Property Tax Paid", "$" + df.format(monthlyPropTax()));
        mortgageRepaymentSummary.put("Total Property Tax Paid", "$" + totalPropTax());
        mortgageRepaymentSummary.put("Monthly Home Insurance", "$" + df.format(monthlyHomeIns()));
        mortgageRepaymentSummary.put("Total Home Insurance", "$" + totalHomeIns());
        mortgageRepaymentSummary.put("Annual Mortgage Payment", "$" + annualMortgagePayment());
        mortgageRepaymentSummary.put("Total of " + firstMortgage.getTotalPayments() + " Payments", "$" + totalMortgage());
    }

}


