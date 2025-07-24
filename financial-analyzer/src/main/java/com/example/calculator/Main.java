package com.example.calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.println("금융 상품 '진짜' 수익률 계산기 (CLI 버전)");
        System.out.println("=======================================");
        System.out.println("원하는 계산기를 선택하세요:");
        System.out.println("1. 구간별 금리 적금 계산기");
        System.out.println("2. 채권 실질 수익률 계산기");
        System.out.print("선택: ");

        int choice = scanner.nextInt();

        if (choice == 1) {
            runSavingsCalculator(scanner, calculator);
        } else if (choice == 2) {
            runBondCalculator(scanner);
        } else {
            System.out.println("잘못된 선택입니다. 프로그램을 종료합니다.");
        }

        scanner.close();
    }

    private static void runSavingsCalculator(Scanner scanner, Calculator calculator) {
        System.out.println("\n--- 구간별 금리 적금 계산기 ---");
        // --- 사용자 입력 받기 ---
        System.out.print("월 납입액 (원): ");
        double monthlyPayment = scanner.nextDouble();

        System.out.print("계약 기간 (개월): ");
        int durationMonths = scanner.nextInt();

        System.out.print("초기 금리 (연, 예: 3.5% -> 0.035): ");
        double rateFirstHalf = scanner.nextDouble();

        System.out.print("우대 금리 (연, 예: 7.0% -> 0.070): ");
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
    }

    private static void runBondCalculator(Scanner scanner) {
        System.out.println("\n--- 채권 실질 수익률 계산기 (세후, 수수료 반영) ---");
        // --- 사용자 입력 받기 ---
        System.out.print("채권 액면가 (원): ");
        double faceValue = scanner.nextDouble();

        System.out.print("채권 매매가 (원): ");
        double purchasePrice = scanner.nextDouble();

        System.out.print("표면 이율 (연, 예: 5% -> 0.05): ");
        double couponRate = scanner.nextDouble();

        System.out.print("만기까지 남은 기간 (년): ");
        double yearsToMaturity = scanner.nextDouble();

        System.out.print("연간 이표 지급 횟수 (예: 1, 2, 4): ");
        int couponFrequency = scanner.nextInt();

        System.out.print("매매 수수료율 (%, 예: 0.1% -> 0.1): ");
        double tradingFeePercentage = scanner.nextDouble();
        double tradingFeeRate = tradingFeePercentage / 100.0; // 백분율을 소수점으로 변환

        // --- 계산 실행 ---
        Bond bond = new Bond(faceValue, purchasePrice, couponRate, yearsToMaturity, couponFrequency, tradingFeeRate);
        double ytm = Calculator.calculateYTM(bond, true); // true: 세금 적용

        // --- 계산 결과 출력 ---
        System.out.println("\n--- 계산 결과 ---");
        System.out.print("입력된 채권 정보로 계산된\n");
        System.out.printf("✨ 실질 만기 수익률 (세후, 수수료 반영): %.4f%%\n", ytm);
    }
}

