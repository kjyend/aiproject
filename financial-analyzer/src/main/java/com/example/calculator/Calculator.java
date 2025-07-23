package com.example.calculator;

public class Calculator {

    private static final double TAX_RATE = 0.154; // 이자 과세율

    /**
     * 구간별 금리 적금의 최종 수령액 및 실질 연이율(APY)을 계산합니다.
     *
     * @param monthlyPayment  월 납입액
     * @param durationMonths  계약 기간 (개월)
     * @param rateFirstHalf   초기 금리 (연)
     * @param rateSecondHalf  우대 금리 (연)
     * @param rateChangeMonth 우대 금리 시작 개월
     * @return 계산 결과 객체 (CalculationResult)
     */
    public CalculationResult calculate(double monthlyPayment, int durationMonths, double rateFirstHalf, double rateSecondHalf, int rateChangeMonth) {
        
        // 1. 세전 총 이자 계산
        double totalInterest = 0;
        for (int month = 1; month <= durationMonths; month++) {
            int monthsRemaining = durationMonths - month + 1;
            double applicableRate = (month < rateChangeMonth) ? rateFirstHalf : rateSecondHalf;
            double interestForThisPayment = monthlyPayment * applicableRate * (monthsRemaining / 12.0);
            totalInterest += interestForThisPayment;
        }

        // 2. 주요 값 계산
        double principal = monthlyPayment * durationMonths;
        double tax = totalInterest * TAX_RATE;
        double afterTaxInterest = totalInterest - tax;
        double finalAmount = principal + afterTaxInterest;

        // 3. 실질 연이율(APY) 계산
        // 총 납입 기간의 중간 시점을 기준으로 평균 원금이 예치되었다고 가정하는 간략화된 계산
        // (총 이자 / 평균 원금) / (총 기간) = 연이율
        double averageInvestedPrincipal = (principal / 2.0);
        double apy = (afterTaxInterest / averageInvestedPrincipal) * (12.0 / durationMonths) * 100;
        
        // 4. 결과 객체 생성 및 반환
        return new CalculationResult(principal, totalInterest, tax, finalAmount, apy);
    }
}
