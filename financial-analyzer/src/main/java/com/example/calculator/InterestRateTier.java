package com.example.calculator;

/**
 * 금리 구조의 단일 구간을 나타냅니다.
 * 예: "처음 6개월 동안은 연 3.5% 금리"
 */
public class InterestRateTier {
    private final int months;       // 이 구간이 적용되는 개월 수
    private final double annualRate; // 이 구간의 연이율

    /**
     * @param months 이 구간이 활성화되는 기간.
     * @param annualRate 이 구간의 연이율 (예: 3.5% -> 3.5 입력).
     */
    public InterestRateTier(int months, double annualRate) {
        this.months = months;
        this.annualRate = annualRate / 100.0; // 3.5 -> 0.035 와 같이 소수점으로 저장
    }

    public int getMonths() {
        return months;
    }

    public double getAnnualRate() {
        return annualRate;
    }
}