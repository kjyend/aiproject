package com.example.calculator;

/**
 * 계산 결과를 담는 데이터 클래스 (Record)
 * @param principal 총 원금
 * @param totalInterest 세전 총 이자
 * @param tax 이자 과세액
 * @param finalAmount 만기 시 실수령액
 * @param apy 실질 연이율
 */
public record CalculationResult(
        double principal,
        double totalInterest,
        double tax,
        double finalAmount,
        double apy
) {
}
