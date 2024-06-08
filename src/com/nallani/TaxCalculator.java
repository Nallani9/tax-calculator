package com.nallani;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaxCalculator {

    // If the length of this rates array is changed, then the bracket arrays in findBracket() must also be changed
    private final static int[] rates = {10, 12, 22, 24, 32, 35};

    //private final static int[] standardDeduction = {12950, 25900, 12950, 19400};
    //2022
    // single, joint, separately, headOfHousehold
    //private final static List<Integer> standardDeduction = new ArrayList<>(Arrays.asList(12950, 25900, 12950, 19400));

    //2023
    // single, joint, separately, headOfHousehold
    //private final static List<Integer> standardDeduction = new ArrayList<>(Arrays.asList(13850, 27700, 13850, 20800));

    //2024
    // single, joint, separately, headOfHousehold
    private final static List<Integer> standardDeduction = new ArrayList<>(Arrays.asList(14600, 29200, 14600, 21,900));

    // Instance Variables
    private int filingStatus;
    private double income;
    private double tax;
    private int[] taxBracket;
    private double fedWithHolding;

    public void findTax() throws Exception {

        // Create a new TaxCalculator object and a Scanner object
        TaxCalculator t = new TaxCalculator();
        Scanner input = new Scanner(System.in);

        // Ask for inputs and store results
        System.out.printf("Please choose filling option form below : \n Enter 0 for single filing. \n Enter 1 for joint filing. \n Enter 2 for separately filing. \n Enter 3 for head of household filing. \n\n Enter the filing status: ");
        t.filingStatus = input.nextInt();
        System.out.printf("Enter the taxable income/wages: ");
        t.income = input.nextDouble();
        System.out.printf("Enter the federal withheld: ");
        t.fedWithHolding = input.nextDouble();

        // We're done asking for inputs, so close the scanner
        input.close();

        // Find the tax brackets based on filingStatus and fill the taxBrackets array with the correct dollar values
        t.taxBracket = findBracket(t.filingStatus);

        //find standard deduction amount
        int standardDeductionAmount =  standardDeduction.get(t.filingStatus);

        //total taxable income after sd
        double totalTaxableAmount = t.income - standardDeductionAmount;

        //Calculate the tax
        t.tax = calculateTax(totalTaxableAmount, t.taxBracket, rates);

        System.out.printf("Federal tax you owe(have to pay) is %.2f", t.tax);
        System.out.printf(" But paid %.2f\n", t.fedWithHolding);
        double totalAmount = t.fedWithHolding - t.tax;
        System.out.printf("Overall percentage is %.2f\n ", + t.tax * 100 / t.income);
        if (t.fedWithHolding >= t.tax) {
            System.out.printf("Congratulations!! You get %.2f\n ", + totalAmount);
        } else {
            System.out.printf("Sorry!! You owe %.2f\n ", + totalAmount);
        }
    }

    // Returns a reference to the proper tax bracket array
    private static int[] findBracket(int filingStatus) throws Exception {
        // Set up the tax bracket values for each filing status based on the maximum dollar amount to fit in a bracket
        // Set each as final, though they could be changed to be set as instance variables if this class was needed in a larger tax program with varying brackets.

/*        //2022
        final int[] single = {10275, 41775, 89075, 170050, 215950, 539900};
        final int[] joint = {20550, 83550, 178150, 340100, 431900, 647850};
        final int[] separately = {10275, 41775, 89075, 170050, 215950, 323925};
        final int[] headOfHousehold = {14650, 55900, 89050, 170050, 215950, 539900};*/

/*        //2023
        final int[] single = {11000, 44725, 95375, 182100, 231250, 578125};
        final int[] joint = {22000, 89450, 190750, 364200, 462500, 693750};
        final int[] separately = {11000, 44725, 95375, 182100, 231250, 578125};
        final int[] headOfHousehold = {15700, 59850, 95350, 182100, 231250, 578100};*/

        //2024
        final int[] single = {11600, 47150, 100525, 191950, 243725, 609350};
        final int[] joint = {23200, 94300, 201050, 383900, 487450, 731200};
        final int[] separately = {11600, 47150, 100525, 191950, 243725, 365600};
        final int[] headOfHousehold = {16550, 63100, 100500, 191950, 243700, 609350};

        // Based on the passed variable filingStatus, return the correct array
        if(filingStatus == 0)
            return single;
        if(filingStatus == 1)
            return joint;
        if(filingStatus == 2)
            return separately;
        if(filingStatus == 3)
            return headOfHousehold;
        else
            // If we have a filingStatus we don't know how to handle, warn the user
            throw new MyException("Unable to match filing status to a tax bracket. Please enter 0, 1, 2, or 3");
    }

    // Calculates the amount of tax owed based on the given income, tax bracket maximums, and tax rates for those brackets.
    private static double calculateTax(double income, int[] taxBrackets, int[] rates){
        double tax = 0;
        int index = taxBrackets.length-1; // By using the array length, this function will scale to any size tax brackets, not just the 6 brackets we currently use

        // Loop through the calculation, taxing each part of the income by its respective bracket and then setting income to the next lowest bracket
        while(index>=0){
            // This will move down a bracket each loop until the given income fits inside a bracket.
            if(income>taxBrackets[index]){
                // Calculate income tax for this bracket only
                tax += (income-taxBrackets[index])*(rates[index+1]/100.00);
                // Set income to the next lowest bracket.
                income = taxBrackets[index];
            }
            index--;
        }

        // Include the final base tax bracket not calculated in the loop that also would handle an instance of having a flat tax.
        tax += income*(rates[0]/100.00);

        // Return the amount of tax owed
        return tax;
    }
}
