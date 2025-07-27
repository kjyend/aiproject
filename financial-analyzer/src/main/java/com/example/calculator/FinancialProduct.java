package com.example.calculator;

/**
 * 계산 가능한 모든 금융 상품에 대한 계약(설계도)을 나타냅니다.
 * 각 특정 상품 클래스(예: TieredSavingsAccount, TDF, Bond)는
 * 이 인터페이스를 구현하여 자신만의 고유한 계산 로직을 제공해야 합니다.
 */
public interface FinancialProduct {
    /**
     * 이 금융 상품의 최종 결과를 계산합니다.
     * 구현체는 이 특정 상품이 어떻게 가치를 축적하고 비용이 발생하는지에 대한
     * 핵심 로직을 포함하게 됩니다.
     *
     * @return 계산 결과를 담은 CalculationResult 객체.
     */
    CalculationResult calculate();
}