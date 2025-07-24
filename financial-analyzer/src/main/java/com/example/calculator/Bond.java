package com.example.calculator;

/**
 * 채권의 속성을 정의하는 데이터 클래스입니다.
 */
public class Bond {

    private final double faceValue;         // 액면가
    private final double purchasePrice;     // 매매가
    private final double couponRate;        // 표면이율 (e.g., 5% -> 0.05)
    private final double yearsToMaturity;   // 만기까지 남은 연수
    private final int couponFrequency;      // 연간 이표 지급 횟수
    private final double tradingFeeRate;    // 매매 수수료율 (e.g., 0.1% -> 0.001)

    /**
     * Bond 객체를 생성합니다.
     *
     * @param faceValue         채권의 액면가
     * @param purchasePrice     채권의 현재 매매가
     * @param couponRate        연간 표면이율 (소수점 형태, 예: 0.05)
     * @param yearsToMaturity   만기까지 남은 기간(연 단위)
     * @param couponFrequency   연간 이표 지급 횟수 (예: 1, 2, 4)
     * @param tradingFeeRate    매매 수수료율 (소수점 형태, 예: 0.001)
     */
    public Bond(double faceValue, double purchasePrice, double couponRate, double yearsToMaturity, int couponFrequency, double tradingFeeRate) {
        this.faceValue = faceValue;
        this.purchasePrice = purchasePrice;
        this.couponRate = couponRate;
        this.yearsToMaturity = yearsToMaturity;
        this.couponFrequency = couponFrequency;
        this.tradingFeeRate = tradingFeeRate;
    }

    // 각 필드에 대한 Getter 메서드
    public double getFaceValue() {
        return faceValue;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public double getCouponRate() {
        return couponRate;
    }

    public double getYearsToMaturity() {
        return yearsToMaturity;
    }

    public int getCouponFrequency() {
        return couponFrequency;
    }

    public double getTradingFeeRate() {
        return tradingFeeRate;
    }
}
