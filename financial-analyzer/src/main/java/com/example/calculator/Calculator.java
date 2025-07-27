package com.example.calculator;

/**
 * 다양한 금융 상품을 위한 범용 계산기입니다.
 * FinancialProduct 인터페이스를 기반으로 동작하므로,
 * 특정 상품의 종류를 알 필요 없이 계약을 준수하는 모든 상품을 계산할 수 있습니다.
 */
public class Calculator {

    /**
     * 주어진 금융 상품에 대한 계산을 수행합니다.
     * 이 메서드는 실제 계산 로직을 상품 객체 자체에 위임합니다.
     *
     * @param product 계산할 금융 상품 (예: TieredSavingsAccount, Bond 객체)
     * @return 상품의 구현에 의해 결정된 계산 결과
     */
    public CalculationResult calculate(FinancialProduct product) {
        // 새로운 설계의 핵심:
        // 계산기는 아무것도 계산하는 방법을 모릅니다.
        // 단지 상품에게 스스로 계산하라고 지시할 뿐입니다.
        return product.calculate();
    }
}
