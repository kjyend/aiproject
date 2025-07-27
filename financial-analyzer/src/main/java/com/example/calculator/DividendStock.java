package com.example.calculator;

import java.util.Map;
import static java.util.Map.entry;

/**
 * 배당주 투자의 실현 수익률을 계산하는 클래스입니다.
 * 매매 차익, 배당금 수익, 그리고 관련된 모든 세금과 수수료를 반영합니다.
 */
public class DividendStock implements FinancialProduct {

    private final double purchasePricePerShare; // 주당 매수 가격
    private final double sellingPricePerShare;  // 주당 매도 가격
    private final int numberOfShares;           // 주식 수
    private final double totalDividendReceived; // 세전 총 수령 배당금
    private final double tradingFeeRate;        // 매매 수수료율 (편도, %)
    private final double securitiesTransactionTaxRate; // 증권거래세율 (%)

    private static final double DIVIDEND_INCOME_TAX_RATE = 0.154; // 배당소득세율 (15.4%)

    public DividendStock(double purchasePricePerShare, double sellingPricePerShare, int numberOfShares,
                         double totalDividendReceived, double tradingFeeRate, double securitiesTransactionTaxRate) {
        this.purchasePricePerShare = purchasePricePerShare;
        this.sellingPricePerShare = sellingPricePerShare;
        this.numberOfShares = numberOfShares;
        this.totalDividendReceived = totalDividendReceived;
        this.tradingFeeRate = tradingFeeRate / 100.0; // 퍼센트를 소수점으로 변환
        this.securitiesTransactionTaxRate = securitiesTransactionTaxRate / 100.0; // 퍼센트를 소수점으로 변환
    }

    @Override
    public CalculationResult calculate() {
        // 1. 매수 관련 비용 계산
        double totalPurchaseAmount = purchasePricePerShare * numberOfShares;
        double purchaseFee = totalPurchaseAmount * tradingFeeRate;
        double totalInvestment = totalPurchaseAmount + purchaseFee;

        // 2. 매도 관련 수익 및 비용 계산
        double totalSellingAmount = sellingPricePerShare * numberOfShares;
        double sellingFee = totalSellingAmount * tradingFeeRate;
        double securitiesTransactionTax = totalSellingAmount * securitiesTransactionTaxRate;

        // 3. 배당 관련 수익 및 세금 계산
        double dividendTax = totalDividendReceived * DIVIDEND_INCOME_TAX_RATE;
        double netDividend = totalDividendReceived - dividendTax;

        // 4. 최종 손익 계산
        double netProfit = (totalSellingAmount - sellingFee - securitiesTransactionTax) + netDividend - totalInvestment;
        double returnOnInvestment = (netProfit / totalInvestment) * 100; // 실현 수익률 (%)

        Map<String, Double> results = Map.ofEntries(
            entry("총 투자 원금 (매수금액 + 수수료)", totalInvestment),
            entry("총 매수 금액", totalPurchaseAmount),
            entry("매수 시 수수료", purchaseFee),
            entry("총 매도 금액", totalSellingAmount),
            entry("매도 시 수수료", sellingFee),
            entry("증권거래세", securitiesTransactionTax),
            entry("세전 총 배당금", totalDividendReceived),
            entry("배당소득세 (15.4%)", dividendTax),
            entry("세후 순수 배당금", netDividend),
            entry("최종 실현 손익", netProfit),
            entry("실현 수익률 (%)", returnOnInvestment)
        );

        return new CalculationResult("배당주 투자", results);
    }
}
