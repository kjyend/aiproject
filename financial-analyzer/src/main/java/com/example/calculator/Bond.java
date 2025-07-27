package com.example.calculator;

import java.util.Map;

/**
 * 채권을 나타내며 FinancialProduct 인터페이스를 구현합니다.
 * 만기 수익률(YTM) 계산 로직이 이 클래스 내에 캡슐화되어 있습니다.
 */
public class Bond implements FinancialProduct {

    private final double faceValue;         // 액면가
    private final double purchasePrice;     // 매입 가격
    private final double couponRate;        // 표면 이율
    private final double yearsToMaturity;   // 만기까지 남은 연수
    private final int couponFrequency;      // 연간 이표 지급 횟수
    private final double tradingFeeRate;    // 매매 수수료율
    private final boolean applyTax;         // 세금 적용 여부

    private static final double TAX_RATE = 0.154; // 이자 소득세

    public Bond(double faceValue, double purchasePrice, double couponRate, double yearsToMaturity, int couponFrequency, double tradingFeeRate, boolean applyTax) {
        this.faceValue = faceValue;
        this.purchasePrice = purchasePrice;
        this.couponRate = couponRate;
        this.yearsToMaturity = yearsToMaturity;
        this.couponFrequency = couponFrequency;
        this.tradingFeeRate = tradingFeeRate;
        this.applyTax = applyTax;
    }

    @Override
    public CalculationResult calculate() {
        double ytm = calculateYTM();
        
        Map<String, Double> results = Map.of(
            "만기 수익률 (YTM, %)", ytm,
            "매입 가격", purchasePrice,
            "액면가", faceValue
        );

        return new CalculationResult("채권", results);
    }

    /**
     * 만기 수익률(YTM)을 계산합니다.
     * @return 계산된 YTM 값 (%)
     */
    private double calculateYTM() {
        final double tolerance = 0.0001; // 계산 정확도 허용 오차
        final int maxIterations = 100;   // 최대 반복 횟수

        // 수수료를 반영한 실제 투자 원금
        double actualPurchasePrice = this.purchasePrice * (1 + this.tradingFeeRate);
        
        double low = 0.0;  // YTM 추정치 하한
        double high = 1.0; // YTM 추정치 상한 (100%)
        double ytm = 0.0;

        // 이분법을 사용하여 YTM 근사치 탐색
        for (int i = 0; i < maxIterations; i++) {
            ytm = (low + high) / 2.0; // 중간값 시도
            double calculatedPrice = calculateBondPrice(ytm);
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
     * 주어진 YTM(할인율)을 사용하여 채권의 현재 가치(가격을)를 계산하는 보조 메서드입니다.
     * @param ytm 테스트할 YTM (연이율)
     * @return 해당 YTM으로 할인된 채권의 현재 가치
     */
    private double calculateBondPrice(double ytm) {
        double price = 0.0;
        // 연간 이표 지급액을 기간별로 나눔
        double periodicCouponPayment = (this.faceValue * this.couponRate) / this.couponFrequency;

        if (this.applyTax) {
            periodicCouponPayment *= (1 - TAX_RATE); // 세금 적용
        }

        double periodicYtm = ytm / this.couponFrequency; // YTM도 기간별로 조정
        int numberOfPeriods = (int) (this.yearsToMaturity * this.couponFrequency);

        // 1. 모든 (세후)이표의 현재 가치를 합산
        for (int t = 1; t <= numberOfPeriods; t++) {
            price += periodicCouponPayment / Math.pow(1 + periodicYtm, t);
        }

        // 2. 만기 시 받을 액면가의 현재 가치를 더함
        price += this.faceValue / Math.pow(1 + periodicYtm, numberOfPeriods);
        return price;
    }

    // 다른 용도로 사용될 수 있으므로 Getter는 남겨둡니다.
    public double getFaceValue() { return faceValue; }
    public double getPurchasePrice() { return purchasePrice; }
    public double getCouponRate() { return couponRate; }
    public double getYearsToMaturity() { return yearsToMaturity; }
    public int getCouponFrequency() { return couponFrequency; }
    public double getTradingFeeRate() { return tradingFeeRate; }
    public boolean isApplyTax() { return applyTax; }
}
