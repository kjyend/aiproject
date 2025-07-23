package com.example.calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.println("금융 상품 '진짜' 수익률 계산기 (CLI 버전)");
        System.out.println("=======================================");

        // --- 사용자 입력 받기 ---
        System.out.print("월 납입액 (원): ");
        double monthlyPayment = scanner.nextDouble();

        System.out.print("계약 기간 (개월): ");
        int durationMonths = scanner.nextInt();

        System.out.print("초기 금리 (연, 예: 0.035): ");
        double rateFirstHalf = scanner.nextDouble();

        System.out.print("우대 금리 (연, 예: 0.070): ");
        double rateSecondHalf = scanner.nextDouble();

        System.out.print("우대 금리 시작 개월 (예: 7개월차부터면 7 입력): ");
        int rateChangeMonth = scanner.nextInt();

        // --- 계산 실행 ---
        CalculationResult result = calculator.calculate(monthlyPayment, durationMonths, rateFirstHalf, rateSecondHalf, rateChangeMonth);

        // --- 계산 결과 출력 ---
        System.out.println("\n--- 계산 결과 ---");
        System.out.printf("총 원금: %,.0f원\n", result.principal());
        System.out.printf("세전 이자: %,.0f원\n", result.totalInterest());
        System.out.printf("이자 과세 (%.1f%%): %,.0f원\n", 15.4, result.tax());
        System.out.printf("만기 시 실수령액: %,.0f원\n", result.finalAmount());
        System.out.printf("\n✨ 실질 연이율 (APY): %.2f%%\n", result.apy());

        scanner.close();
    }
}

