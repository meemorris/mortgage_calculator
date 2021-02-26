package com.bankciti;

import com.bankciti.exception.DateFormatException;
import com.bankciti.exception.InvalidRateException;
import com.bankciti.exception.MortgageTermException;
import com.bankciti.exception.PositiveNumberException;
import com.bankciti.mortgage.AmortizationCalculator;
import com.bankciti.mortgage.House;
import com.bankciti.mortgage.Mortgage;
import com.bankciti.mortgage.MortgageCalculator;
import com.bankciti.view.MenuDrivenCLI;

import java.math.BigDecimal;
import java.util.Map;


public class Application {

    private static final String MENU_OPTION_MORTGAGE_CALCULATOR = "Mortgage Calculator";
    private static final String MENU_OPTION_REPAYMENT_SUMMARY = "View Mortgage Repayment Summary";
    private static final String MENU_OPTION_AMORTIZATION_SCHEDULE = "View Amortization Schedule";
    private static final String MENU_OPTION_QUIT = "Exit";
    private static final String[] MAIN_MENU_OPTIONS = {MENU_OPTION_MORTGAGE_CALCULATOR, MENU_OPTION_REPAYMENT_SUMMARY,
            MENU_OPTION_AMORTIZATION_SCHEDULE, MENU_OPTION_QUIT};

    private final MenuDrivenCLI ui = new MenuDrivenCLI();

    private House firstHome = new House();
    private Mortgage firstMortgage = new Mortgage();
    private MortgageCalculator calc = new MortgageCalculator(firstMortgage, firstHome);
    private AmortizationCalculator amortization = new AmortizationCalculator(firstMortgage, firstHome);


    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }

    public void run() {

        boolean running = true;
        while (running) {
            String selection = ui.promptForSelection(MAIN_MENU_OPTIONS);
            if (selection.equals(MENU_OPTION_MORTGAGE_CALCULATOR)) {
                calcMortgage();
            } else if (selection.equals(MENU_OPTION_REPAYMENT_SUMMARY)) {
                handleMortgageRepaymentSummary();
            } else if (selection.equals(MENU_OPTION_AMORTIZATION_SCHEDULE)) {
                handleAmortizationSchedule();
            } else if (selection.equals(MENU_OPTION_QUIT)) {
                running = false;
            }
        }
    }

    private void calcMortgage() {

        ui.output("All currency amounts you provide are required in USD.");

        boolean finished = false;
        while (!finished) {

            handleHomeValue();
            handleDownPayment();
            handlePrincipal();
            handleInterestRate();
            handleLoanTerm();

            promptStartDate();
            promptPmiRate();
            handleAnnualPropTax();
            handleAnnualHomeIns();
            promptHoaFee();

            ui.output("∞∞∞∞∞∞∞∞ " + "Monthly Mortgage Payment: $" +
                    calc.monthlyMortgage() + " ∞∞∞∞∞∞∞∞");

            finished = true;
        }
        ui.pauseOutput();
    }


    private void handleMortgageRepaymentSummary() {
        try {
            calc.repaymentSummary();
            ui.output(displaySummary());
            ui.pauseOutput();
        } catch (NullPointerException e) {
            ui.output("Please select mortgage calculator first in order to calculate necessary data.");
        }
    }

    private void handleAmortizationSchedule() {
        try {
            ui.output(amortization.calcAmortization());
            ui.pauseOutput();
        } catch (NullPointerException e) {
            ui.output("Please select mortgage calculator first in order to calculate necessary data.");
        }
    }

    private void handleHomeValue() {
        boolean finished = false;
        while (!finished) {
            try {
                firstHome.setHomeValue(ui.returnMonetary("Home Value? "));
                if (firstHome.getHomeValue().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }


    private void handleDownPayment() {
        boolean finished = false;
        while (!finished) {
            try {
                firstHome.setDownPayment(ui.returnMonetary("Down Payment? "));
                if (firstHome.getDownPayment().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private void handlePrincipal() {
        firstMortgage.setStartingPrincipal(firstHome.getHomeValue().subtract(firstHome.getDownPayment()));
    }

    private void handleInterestRate() {
        boolean finished = false;
        while (!finished) {
            try {
                String response = ui.promptForString("Annual Interest Rate? (x.xx) ");
                BigDecimal value = new BigDecimal(response);
                firstMortgage.setInterestRate(value.movePointLeft(2));
                if (response.length() > 5 || response.length() < 3 || !response.contains(".")) {
                    throw new InvalidRateException();
                } else if (firstMortgage.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (InvalidRateException | PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private void handleLoanTerm() {
        boolean finished = false;
        while (!finished) {
            try {
                firstMortgage.setLoanTerm(Integer.parseInt(ui.returnInput("Loan Term? (Provide term length in years) ")));
                if (firstMortgage.getLoanTerm() > 50) {
                    throw new MortgageTermException();
                } else if (firstMortgage.getLoanTerm() < 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (MortgageTermException | PositiveNumberException ex) {
                ui.output(ex.getMessage());
            }
        }
    }

    private void promptStartDate() {
        boolean finished = false;
        while (!finished) {
            try {
                String response = ui.returnInput("Start Date? (MM/YYYY) ");

                if (!response.contains("/") || response.length() != 7) {
                    throw new DateFormatException();
                } else {
                    String[] arrayStartDate = response.split("/");
                    firstMortgage.setStartMonthNum(Integer.parseInt(arrayStartDate[0]));
                    handleNumToMonth();
                    firstMortgage.setStartYear(Integer.parseInt(arrayStartDate[1]));
                    finished = true;
                }
            } catch (DateFormatException ex) {
                ui.output(ex.getMessage());
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            }
        }
    }

    private void handleNumToMonth() {
        int monthNum = firstMortgage.getStartMonthNum();
        if (calc.months.containsKey(monthNum)) {
            firstMortgage.setStartMonth(calc.months.get(monthNum));
        }
    }

    private void promptPmiRate() {
        boolean finished = false;
        while (!finished) {
            try {
                String response = ui.promptForString("Are you required to have Private Mortgage " +
                        "Insurance (PMI)? \nIf yes, please enter annual PMI rate. (x.xx)  \nIf N/A, please enter zero. ");
                BigDecimal value = new BigDecimal(response);
                firstMortgage.setPmiRate(value.movePointLeft(2));

                if (firstMortgage.getPmiRate().compareTo(BigDecimal.ZERO) == 0) {
                    finished = true;
                } else if (response.length() > 5 || response.length() < 3 || !response.contains(".")) {
                    throw new InvalidRateException();
                } else if (firstMortgage.getPmiRate().compareTo(BigDecimal.ZERO) < 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (InvalidRateException | PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private void handleAnnualPropTax() {
        boolean finished = false;
        while (!finished) {
            try {
                firstHome.setAnnualPropTax(ui.returnMonetary("Annual Property Tax amount? "));
                if (firstHome.getAnnualPropTax().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private void handleAnnualHomeIns() {
        boolean finished = false;
        while (!finished) {
            try {
                firstHome.setHomeIns(ui.returnMonetary("Annual Homeowner's Insurance premium? "));
                if (firstHome.getHomeIns().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private void promptHoaFee() {
        boolean finished = false;
        while (!finished) {
            try {
                firstHome.setAnnualHoa(ui.returnMonetary("Annual HOA amount? \nIf N/A, please enter zero. "));
                if (firstHome.getAnnualHoa().compareTo(BigDecimal.ZERO) == 0) {
                    finished = true;
                } else if (firstHome.getAnnualHoa().compareTo(BigDecimal.ZERO) < 0) {
                    throw new PositiveNumberException();
                } else {
                    finished = true;
                }
            } catch (NumberFormatException e) {
                ui.outputForNumberFormat();
            } catch (PositiveNumberException e) {
                ui.output(e.getMessage());
            }
        }
    }

    private String displaySummary() {
        StringBuilder summary = new StringBuilder();

        for (Map.Entry<String, String> item : calc.mortgageRepaymentSummary.entrySet()) {
            summary.append(item.getValue()).append("\n").append(item.getKey()).append("\n\n");
        }
        return summary.toString();
    }
}



