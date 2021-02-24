package com.bigbank.testassignment.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import com.bigbank.testassignment.client.LoanCalculator;
import com.bigbank.testassignment.shared.Payment;
import com.bigbank.testassignment.shared.Request;


public class Services {

	public static Payment calculateLoanPayments (Request request) {

		// method receives Request object
		// calculates and returns payments as an Payment object

		// initiate return object
		Payment transferPayment = new Payment();

		// input variables to bigdecimal, months stay 
		BigDecimal calcRequiredLoanAmount = BigDecimal.valueOf(request.getAmount());
		Long calcRequiredMonths = request.getMonths();
		BigDecimal calcRequiredInterest = BigDecimal.valueOf(request.getInterest());

		// initiate additional calculated variables
		BigDecimal calcLoanSum = BigDecimal.ZERO;
		BigDecimal calcInterestPayment = BigDecimal.ZERO;
		BigDecimal principalPayment = BigDecimal.ZERO;
		BigDecimal calcTotalPayment = BigDecimal.ZERO; 
		BigDecimal principalRemaining = calcRequiredLoanAmount;

		// set bigdecimal scale for calculating variables
		calcLoanSum.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		calcInterestPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		principalPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		calcTotalPayment.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		principalRemaining.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		// start counting months
		Calendar now = Calendar.getInstance();
		calcRequiredMonths = (long) now.get(Calendar.MONTH);

		// calculate payment monthly sum
		double fraction = (BigDecimal.ONE.add(calcRequiredInterest)).doubleValue();
		double period = calcRequiredMonths.doubleValue();
		double fractionSquare = Math.pow(fraction, -period);
		BigDecimal partion = new BigDecimal (fractionSquare);
		calcTotalPayment = calcRequiredInterest.multiply(calcRequiredLoanAmount).
				divide((BigDecimal.ONE).subtract(partion));

		// set up counter, create list objects 
		Long transferCounter = Long.valueOf(1);

		ArrayList <Long> calculatedNumberList = new ArrayList<Long>();
		ArrayList <Long> calculatedMonthList = new ArrayList<Long>();
		ArrayList <BigDecimal> calculatedLoanSumList = new ArrayList<BigDecimal>();
		ArrayList <BigDecimal> calculatedInterestPaymentList = new ArrayList<BigDecimal>();
		ArrayList <BigDecimal> calculatedPrincipalPaymentList = new ArrayList<BigDecimal>();
		ArrayList <BigDecimal> calculatedTotalPaymentList = new ArrayList<BigDecimal>();
		ArrayList <BigDecimal> calculatedPrincipalRemainingList = new ArrayList<BigDecimal>();

		BigDecimal twelve = new BigDecimal(12);

		// loop through months, calculate and add payment variables to Payment object
		for (int i = 0; i < request.getMonths(); i++) {

			// calculate monthly interest
			calcInterestPayment = calcRequiredLoanAmount.multiply((BigDecimal.ONE.add
					(calcRequiredLoanAmount.movePointLeft(2).divide(twelve)))).subtract(principalRemaining);

			// calculate principal
			principalPayment = calcTotalPayment.subtract(calcInterestPayment);

			calculatedNumberList.add(transferCounter);
			calculatedMonthList.add(calcRequiredMonths);
			calculatedLoanSumList.add(principalRemaining);
			calculatedInterestPaymentList.add(calcInterestPayment);
			calculatedPrincipalPaymentList.add(principalPayment);
			calculatedTotalPaymentList.add(calcTotalPayment);

			// recalculating principal
			principalRemaining = principalRemaining.subtract(calcTotalPayment);

			calculatedPrincipalRemainingList.add(principalRemaining);

			calcRequiredMonths ++;
			
			transferCounter ++;

		}

		transferPayment.setNumber(calculatedNumberList);
		transferPayment.setMonth(calculatedMonthList);
		transferPayment.setLoanSum(calculatedLoanSumList);
		transferPayment.setInterestPayment(calculatedInterestPaymentList);
		transferPayment.setPrincipalPayment(calculatedPrincipalPaymentList);
		transferPayment.setTotalPayment(calculatedTotalPaymentList);
		transferPayment.setPrincipalRemaining(calculatedPrincipalRemainingList);

		LoanCalculator.updatePaymentsTable(transferPayment);

		return transferPayment;

	}

}
