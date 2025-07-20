package com.example.calculator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

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

        double taxRate = 0.154; // 세금 (일단 고정)

        // --- 계산 로직 구현 ---
        double totalInterest = 0; // 총 이자 (세전)
        
        for (int month = 1; month <= durationMonths; month++) {
            int monthsRemaining = durationMonths - month + 1;
            
            double applicableRate = (month < rateChangeMonth) ? rateFirstHalf : rateSecondHalf;
            
            double interestForThisPayment = monthlyPayment * applicableRate * (monthsRemaining / 12.0);
            
            totalInterest += interestForThisPayment;
        }

        double principal = monthlyPayment * durationMonths; // 총 원금
        double totalAfterTaxInterest = totalInterest * (1 - taxRate); // 세후 이자
        double finalAmount = principal + totalAfterTaxInterest; // 최종 수령액

        // 실질 연이율(APY) 계산
        // 총 납입 기간의 중간 시점을 기준으로 평균 원금이 예치되었다고 가정하는 간략화된 계산
        double averagePrincipal = (principal / 2.0) * (durationMonths / 12.0);
        double apy = (totalAfterTaxInterest / averagePrincipal) * (1.0 / (durationMonths / 12.0)) * 100;


        System.out.println("\n--- 계산 결과 ---");
        System.out.printf("총 원금: %,.0f원\n", principal);
        System.out.printf("세전 이자: %,.0f원\n", totalInterest);
        System.out.printf("이자 과세 (%.1f%%): %,.0f원\n", taxRate * 100, totalInterest * taxRate);
        System.out.printf("만기 시 실수령액: %,.0f원\n", finalAmount);
        System.out.printf("\n✨ 실질 연이율 (APY): %.2f%%\n", apy);

        scanner.close();
    }
}

