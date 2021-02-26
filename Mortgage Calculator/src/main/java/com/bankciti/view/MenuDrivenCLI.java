package com.bankciti.view;

import java.math.BigDecimal;
import java.util.Scanner;

public class MenuDrivenCLI {

    private final Scanner input = new Scanner(System.in);
    private final Menu menu = new Menu(System.in, System.out);

    public void output(String content) {
        System.out.println();
        System.out.println(content);
    }

    public String returnInput(String content) {
        System.out.println();
        System.out.print("► " + content);
        return input.nextLine();
    }

    public BigDecimal returnMonetary(String content) {
        System.out.println();
        System.out.print("► " + content);
        String providedValue = input.nextLine();
        return formatMonetary(providedValue);
    }

    public void pauseOutput() {
        System.out.println();
        System.out.println("Press Enter to continue...");
        input.nextLine();
    }

    public String promptForSelection(String[] options) {
        return (String) menu.getChoiceFromOptions(options);
    }

    public String promptForString(String content) {
        System.out.println();
        System.out.print(content);
        return input.nextLine();
    }

    public BigDecimal formatMonetary(String providedValue) {
        String valueNumeric = providedValue.replace(",", "");
        if (valueNumeric.contains("$")) {
            valueNumeric = valueNumeric.replace("$", "");
        }
        if (!valueNumeric.contains(".")) {
            valueNumeric = valueNumeric + ".00";
        }
        return new BigDecimal(valueNumeric);
    }

    public void outputForNumberFormat() {
        System.out.println();
        System.out.println("*** That's not a valid option. Please enter numeric values. ***");
    }

}
