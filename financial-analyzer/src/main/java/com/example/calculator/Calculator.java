package com.example.calculator;

public class Calculator {

    private static final double TAX_RATE = 0.154; // 이자 과세율

    /**
     * 구간별 금리 적금의 최종 수령액 및 실질 연이율(APY)을 계산합니다.
     * (이하 생략 - 기존 코드와 동일)
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
        double averageInvestedPrincipal = (principal / 2.0);
        double apy = (afterTaxInterest / averageInvestedPrincipal) * (12.0 / durationMonths) * 100;
        
        // 4. 결과 객체 생성 및 반환
        return new CalculationResult(principal, totalInterest, tax, finalAmount, apy);
    }

    /**
     * 채권의 실질 만기 수익률(YTM)을 계산합니다.
     * 매매 수수료와 이자 소득세를 모두 반영하여 실제 투자자 관점의 수익률을 계산합니다.
     *
     * @param bond     계산할 채권 객체
     * @param applyTax 이자 소득세(15.4%) 적용 여부
     * @return 계산된 실질 만기 수익률 (YTM)을 퍼센트 단위로 반환
     */
    public static double calculateYTM(Bond bond, boolean applyTax) {
        final double tolerance = 0.0001; // 계산 정확도
        final int maxIterations = 100;   // 최대 반복 횟수

        // 수수료를 반영한 실제 투자 원금 계산
        double actualPurchasePrice = bond.getPurchasePrice() * (1 + bond.getTradingFeeRate());

        double low = 0.0;  // YTM 추정치의 하한
        double high = 1.0; // YTM 추정치의 상한 (100%)
        double ytm = 0.0;

        for (int i = 0; i < maxIterations; i++) {
            ytm = (low + high) / 2.0; // 중간값 시도
            double calculatedPrice = calculateBondPrice(ytm, bond, applyTax);
            double priceDifference = calculatedPrice - actualPurchasePrice;

            if (Math.abs(priceDifference) < tolerance) {
                return ytm * 100; // 허용 오차 내에 들어오면 성공
            }

            if (priceDifference > 0) {
                low = ytm;
            } else {
                high = ytm;
            }
        }
        return ytm * 100; // 최대 반복 후 최종 근사치 반환
    }

    /**
     * 주어진 YTM(할인율)을 사용하여 채권의 현재 가치(가격)를 계산하는 보조 메서드입니다.
     *
     * @param ytm      테스트할 YTM (연이율)
     * @param bond     채권 객체
     * @param applyTax 이자 소득세 적용 여부
     * @return 해당 YTM으로 할인된 채권의 현재 가치
     */
    private static double calculateBondPrice(double ytm, Bond bond, boolean applyTax) {
        double price = 0.0;
        double faceValue = bond.getFaceValue();
        double couponRate = bond.getCouponRate();
        double yearsToMaturity = bond.getYearsToMaturity();
        int couponFrequency = bond.getCouponFrequency();

        double periodicCouponPayment = (faceValue * couponRate) / couponFrequency;
        
        // 세금 적용 옵션에 따라 실제 수령할 이표 금액 조정
        if (applyTax) {
            periodicCouponPayment *= (1 - TAX_RATE);
        }

        double periodicYtm = ytm / couponFrequency;
        int numberOfPeriods = (int) (yearsToMaturity * couponFrequency);

        // 1. 모든 (세후)이표의 현재 가치를 합산
        for (int t = 1; t <= numberOfPeriods; t++) {
            price += periodicCouponPayment / Math.pow(1 + periodicYtm, t);
        }

        // 2. 만기 시 받을 액면가의 현재 가치를 더함
        price += faceValue / Math.pow(1 + periodicYtm, numberOfPeriods);

        return price;
    }
}
