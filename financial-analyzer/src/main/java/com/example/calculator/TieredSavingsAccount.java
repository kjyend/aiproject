package com.example.calculator;

import java.util.List;
import java.util.Map;

/**
 * 기간별 차등 금리를 적용하는 적금 계좌를 나타냅니다.
 * 이 클래스는 FinancialProduct 인터페이스를 구현하며, 이 유형의 계좌에 대한
 * 구체적인 계산 로직을 제공합니다.
 */
public class TieredSavingsAccount implements FinancialProduct {

    private final double monthlyDeposit;
    private final List<InterestRateTier> interestRateTiers;
    private static final double TAX_RATE = 0.154; // 이자 소득세 15.4%

    public TieredSavingsAccount(double monthlyDeposit, List<InterestRateTier> interestRateTiers) {
        this.monthlyDeposit = monthlyDeposit;
        this.interestRateTiers = interestRateTiers;
    }

    @Override
    public CalculationResult calculate() {
        double principal = 0; // 총 원금
        double totalInterest = 0; // 세전 총 이자
        int totalMonths = 0;
        for(InterestRateTier tier : interestRateTiers) {
            totalMonths += tier.getMonths();
        }

        // 각 입금 회차별로 예치 기간을 계산하여 이자 계산 (단리 방식)
        // 은행의 실제 적금 계산 방식과 유사합니다.
        for (int month = 1; month <= totalMonths; month++) {
            principal += monthlyDeposit;
            double interestForThisDeposit = 0;
            int monthsRemaining = totalMonths - month + 1; // 이 예치금이 머무를 개월 수

            int elapsedMonthsInTiers = 0;
            // 각 금리 구간을 순회하며 이자를 계산
            for (InterestRateTier tier : interestRateTiers) {
                // 현재 회차의 예치금이 현재 금리 구간에 얼마나 머무르는지 계산
                int monthsInThisTier = Math.min(tier.getMonths(), Math.max(0, monthsRemaining - elapsedMonthsInTiers));
                
                if (monthsInThisTier > 0) {
                    // (원금 * 연이율 * (예치개월수/12)) 공식으로 단리 계산
                    interestForThisDeposit += monthlyDeposit * tier.getAnnualRate() * (monthsInThisTier / 12.0);
                }
                elapsedMonthsInTiers += tier.getMonths();
            }
            totalInterest += interestForThisDeposit;
        }

        double tax = totalInterest * TAX_RATE;
        double afterTaxInterest = totalInterest - tax;
        double finalAmount = principal + afterTaxInterest;

        // 실효 수익률 (APY) 계산
        // (세후이자 / 총원금) * (12 / (총개월수+1)) * 2 * 100
        // 단리 적금의 실효 수익률을 구하는 간편 공식입니다.
        double effectiveApy = (afterTaxInterest / principal) * (12.0 / (totalMonths + 1)) * 2 * 100;

        Map<String, Double> results = Map.of(
            "총 원금", principal,
            "세전 총 이자", totalInterest,
            "이자 소득세 (15.4%)", tax,
            "세후 실수령액", finalAmount,
            "실효 수익률 (연, %)", effectiveApy
        );

        return new CalculationResult("기간별 차등금리 적금", results);
    }
}