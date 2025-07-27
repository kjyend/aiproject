package com.example.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.println("금융 상품 '진짜' 수익률 계산기");
        System.out.println("=======================================");

        while (true) {
            System.out.println("\n원하는 계산기를 선택하세요 (종료하려면 0 입력):");
            System.out.println("1. 기간별 차등금리 적금 계산기");
            System.out.println("2. 채권 실질 수익률 계산기");
            System.out.print("선택: ");

            int choice = scanner.nextInt();

            if (choice == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            FinancialProduct product = null;
            switch (choice) {
                case 1:
                    product = createSavingsAccountFromUserInput(scanner);
                    break;
                case 2:
                    product = createBondFromUserInput(scanner);
                    break;
                default:
                    System.out.println("잘못된 선택입니다. 다시 시도해주세요.");
                    continue;
            }

            if (product != null) {
                CalculationResult result = calculator.calculate(product);
                System.out.println(result);
            }
        }

        scanner.close();
    }

    private static FinancialProduct createSavingsAccountFromUserInput(Scanner scanner) {
        System.out.println("\n--- 기간별 차등금리 적금 계산기 ---");
        System.out.print("월 납입액 (원): ");
        double monthlyDeposit = scanner.nextDouble();

        List<InterestRateTier> tiers = new ArrayList<>();
        while (true) {
            System.out.printf("\n--- %d번째 금리 구간 --- (종료하려면 기간에 0 입력)\n", tiers.size() + 1);
            System.out.print("적용 개월 수: ");
            int months = scanner.nextInt();
            if (months == 0) {
                break;
            }
            System.out.print("연 금리 (%): ");
            double rate = scanner.nextDouble();
            tiers.add(new InterestRateTier(months, rate));
        }

        if (tiers.isEmpty()) {
            System.out.println("금리 구간이 입력되지 않았습니다. 메인 메뉴로 돌아갑니다.");
            return null;
        }

        return new TieredSavingsAccount(monthlyDeposit, tiers);
    }

    private static FinancialProduct createBondFromUserInput(Scanner scanner) {
        System.out.println("\n--- 채권 실질 수익률 계산기 ---");
        System.out.print("채권 액면가 (원): ");
        double faceValue = scanner.nextDouble();

        System.out.print("채권 매입 가격 (원): ");
        double purchasePrice = scanner.nextDouble();

        System.out.print("표면 이율 (연, %): ");
        double couponRate = scanner.nextDouble() / 100.0;

        System.out.print("만기까지 남은 기간 (년): ");
        double yearsToMaturity = scanner.nextDouble();

        System.out.print("연간 이표 지급 횟수 (예: 1, 2, 4): ");
        int couponFrequency = scanner.nextInt();

        System.out.print("매매 수수료율 (%, 예: 0.1% -> 0.1): ");
        double tradingFeeRate = scanner.nextDouble() / 100.0;

        System.out.print("이자 소득세(15.4%) 적용 여부 (true/false): ");
        boolean applyTax = scanner.nextBoolean();

        return new Bond(faceValue, purchasePrice, couponRate, yearsToMaturity, couponFrequency, tradingFeeRate, applyTax);
    }
}
