package com.bigbank.testassignment.repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bigbank.testassignment.server.ConnectionPool;
import com.bigbank.testassignment.shared.Payment;


public class Repository {

	private static final Logger logger = Logger.getLogger(Repository.class);
	private static Repository instance;
	private ConnectionPool pool;

	public static Repository getInstance() {

		if (instance == null) {
			instance = new Repository();
		}

		return instance;
	}

	
	public Repository() {

		pool = new ConnectionPool();
		pool.start();
	}


	public void addPaymentToDatabase(Payment payment) {

		logger.info("Writing payment data to database - initiated");

		try {
			PreparedStatement pstmt = pool.getConnection().prepareStatement(
					"INSERT INTO bigbankLoanDatabase(nr, payment_month, monthly payment, "
							+ "principal payment, interest, principal remaining) VALUES(?,?,?,?,?,?)",
							ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

			final Long[] numberDataToSql = payment.getNumber().toArray(new Long[payment.getNumber().size()]);
			final java.sql.Array numberSqlArray = pool.getConnection().createArrayOf("bigint", numberDataToSql);
			pstmt.setObject(1, numberSqlArray);

			final Long[] monthDataToSql = payment.getMonth().toArray(new Long[payment.getMonth().size()]);
			final java.sql.Array monthSqlArray = pool.getConnection().createArrayOf("bigint", monthDataToSql);
			pstmt.setObject(2, monthSqlArray);

			final BigDecimal[] totalPaymentDataToSql = payment.getTotalPayment().toArray(new BigDecimal[payment.getTotalPayment().size()]);
			final java.sql.Array totalPaymentSqlArray = pool.getConnection().createArrayOf("bigint", totalPaymentDataToSql);
			pstmt.setObject(3, totalPaymentSqlArray);

			final BigDecimal[] principalPaymentDataToSql = payment.getPrincipalPayment().toArray(new BigDecimal[payment.getPrincipalPayment().size()]);
			final java.sql.Array principalPaymentSqlArray = pool.getConnection().createArrayOf("bigint", principalPaymentDataToSql);
			pstmt.setObject(4, principalPaymentSqlArray);

			final BigDecimal[] interestPaymentDataToSql = payment.getInterestPayment().toArray(new BigDecimal[payment.getInterestPayment().size()]);
			final java.sql.Array interestPaymentSqlArray = pool.getConnection().createArrayOf("bigint", interestPaymentDataToSql);
			pstmt.setObject(5, interestPaymentSqlArray);

			final BigDecimal[] principalRemainingDataToSql = payment.getPrincipalRemaining().toArray(new BigDecimal[payment.getPrincipalRemaining().size()]);
			final java.sql.Array principalRemainingSqlArray = pool.getConnection().createArrayOf("bigint", principalRemainingDataToSql);
			pstmt.setObject(6, principalRemainingSqlArray);

			pstmt.executeUpdate();
			logger.info("Writing payment data to database - success");

		} catch (SQLException ex) { logger.error(ex.getMessage()); logger.info("Writing payment data to database - fail"); }

	}

}
