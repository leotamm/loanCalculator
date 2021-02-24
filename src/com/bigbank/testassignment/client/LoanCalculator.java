package com.bigbank.testassignment.client;

import com.bigbank.testassignment.repository.Repository;
import com.bigbank.testassignment.server.Services;
import com.bigbank.testassignment.shared.Payment;
import com.bigbank.testassignment.shared.Request;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class LoanCalculator extends Composite implements EntryPoint {

	private static LoanCalculatorUiBinder uiBinder = GWT.create(LoanCalculatorUiBinder.class);
	interface LoanCalculatorUiBinder extends UiBinder<Widget, LoanCalculator> {
	}


	@UiField
	TextBox textLoanAmount;
	@UiField 
	TextBox textPeriodInMonths;
	@UiField
	TextBox textYearInterestRate;
	@UiField
	Button calculatePayments;
	@UiField
	static FlexTable paymentsTable;
	

	@UiHandler("calculatePayments")
	void onClick(ClickEvent eventCalculatePayments) {
		
		// read amount, months, interest from UI fields
		String amountIn = null;
		amountIn = textLoanAmount.getText();
		String monthsIn;
		monthsIn = textPeriodInMonths.getText();
		String interestIn;
		interestIn = textYearInterestRate.getText();

		// check if input data is not empty
		if (!amountIn.isEmpty() && !monthsIn.isEmpty() && !interestIn.isEmpty()) {
			
			Long amount = null;
			amount = Long.parseLong(textLoanAmount.getText());
			Long months = null;
			months = Long.parseLong(textPeriodInMonths.getText());
			Long interest = null;
			interest = Long.parseLong(textYearInterestRate.getText());

			// check if input data is numeric type
			if (amount.getClass().equals(Long.class) 
					&& months.getClass().equals(Long.class) 
					&& amount.getClass().equals(Long.class)) {

				// check if numeric value of amount, months larger than 0
				if (amount > 0 && months > 0 && interest >= 0) {

					Request request = new Request();
					request.setAmount(amount);
					request.setMonths(months);
					request.setInterest(interest);
					
					// all good - clear UI input fields
					clearInputFields();
					
					// initiate return object
					Payment returnedPayment = new Payment();
					
					// calculate monthly payments in server
					returnedPayment = Services.calculateLoanPayments(request);
					
					// populate UI table
					updatePaymentsTable(returnedPayment);
					
					// write the Payment object to postgresql database
					Repository.getInstance().addPaymentToDatabase(returnedPayment);
					
				} else { Window.alert("These numbers do not make sense"); clearInputFields(); }

			} else { Window.alert("Please use numeric values"); clearInputFields(); }

		} else { Window.alert("Please fill all fields");  clearInputFields(); }

	}

	public static void updatePaymentsTable(Payment payment) {
		
		int tableRowCounter = 1;
		setUpPaymentTableHeader();
		
		paymentsTable.insertRow(tableRowCounter);
		paymentsTable.setHTML(tableRowCounter, 0, "<h6>" + String.valueOf(payment.getNumber()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 1, "<h6>" + String.valueOf(payment.getMonth()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 2, "<h6>" + String.valueOf(payment.getLoanSum()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 3, "<h6>" + String.valueOf(payment.getInterestPayment()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 4, "<h6>" + String.valueOf(payment.getPrincipalPayment()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 5, "<h6>" + String.valueOf(payment.getTotalPayment()) + "</h6>");
		paymentsTable.setHTML(tableRowCounter, 6, "<h6>" + String.valueOf(payment.getPrincipalRemaining()) + "</h6>");
		
	}
	
	public static void setUpPaymentTableHeader() {
		
		paymentsTable.clear();
		paymentsTable.removeAllRows();
		paymentsTable.insertRow(0);
		paymentsTable.setHTML(0, 0, "<h6>Nr</h6>");
		paymentsTable.setHTML(0, 1, "<h6>Kuup‰ev</h6>");
		paymentsTable.setHTML(0, 2, "<h6>Laenusumma Ä</h6>");
		paymentsTable.setHTML(0, 3, "<h6>Intress Ä</h6>");
		paymentsTable.setHTML(0, 4, "<h6>Pıhiosa makse Ä</h6>");
		paymentsTable.setHTML(0, 5, "<h6>Makse kokku Ä</h6>");
		paymentsTable.setHTML(0, 6, "<h6>Laenuj‰‰k Ä</h6>");

	}

	
	public void clearInputFields() {

		textLoanAmount.setText("");
		textPeriodInMonths.setText("");
		textYearInterestRate.setText("");

	}


	public void onModuleLoad() {

		initWidget(uiBinder.createAndBindUi(this));
		RootPanel.get().add(this);

		paymentsTable.insertRow(0);
		paymentsTable.setHTML(0, 0, "<h6>Select amount, period, interest above and press <kbd>Calculate payment</kbd></h6>");

	}
	
}
