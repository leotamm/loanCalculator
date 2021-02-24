package com.bigbank.testassignment.shared;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Payment {
	
	private ArrayList <Long> Number;					// jrk nr
	private ArrayList <Long> Month;						// makse kuu
	private ArrayList <BigDecimal> loanSum;				// laenu suurus
	private ArrayList <BigDecimal> interestPayment;		// intressi suurus
	private ArrayList <BigDecimal> principalPayment;	// p6hiosa suurus
	private ArrayList <BigDecimal> totalPayment;		// kuumakse suurus
	private ArrayList <BigDecimal> principalRemaining;	// laenu jaak
		
	public ArrayList <Long> getNumber() {
		return Number;
	}
	public void setNumber(ArrayList <Long> number) {
		Number = number;
	}
	public ArrayList <Long> getMonth() {
		return Month;
	}
	public void setMonth(ArrayList <Long> month) {
		Month = month;
	}
	public ArrayList <BigDecimal> getLoanSum() {
		return loanSum;
	}
	public void setLoanSum(ArrayList <BigDecimal> loanSum) {
		this.loanSum = loanSum;
	}
	public ArrayList <BigDecimal> getInterestPayment() {
		return interestPayment;
	}
	public void setInterestPayment(ArrayList <BigDecimal> interestPayment) {
		this.interestPayment = interestPayment;
	}
	public ArrayList <BigDecimal> getPrincipalPayment() {
		return principalPayment;
	}
	public void setPrincipalPayment(ArrayList <BigDecimal> principalPayment) {
		this.principalPayment = principalPayment;
	}
	public ArrayList <BigDecimal> getTotalPayment() {
		return totalPayment;
	}
	public void setTotalPayment(ArrayList <BigDecimal> totalPayment) {
		this.totalPayment = totalPayment;
	}
	public ArrayList <BigDecimal> getPrincipalRemaining() {
		return principalRemaining;
	}
	public void setPrincipalRemaining(ArrayList <BigDecimal> principalRemaining) {
		this.principalRemaining = principalRemaining;
	}
	
}
