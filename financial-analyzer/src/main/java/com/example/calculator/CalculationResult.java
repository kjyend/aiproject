package com.example.calculator;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 모든 금융 상품의 계산 결과를 담는 유연한 컨테이너입니다.
 * Map을 사용하여 "최종 금액", "YTM" 등 다양한 이름의 지표를 저장하고,
 * 결과를 한글 형식으로 출력합니다.
 */
public class CalculationResult {

    private final String productName;
    private final Map<String, Double> results;

    public CalculationResult(String productName, Map<String, Double> results) {
        this.productName = productName;
        this.results = Collections.unmodifiableMap(results); // 불변 Map으로 만듦
    }

    public String getProductName() {
        return productName;
    }

    public Map<String, Double> getResults() {
        return results;
    }

    @Override
    public String toString() {
        // 숫자를 원화(KRW) 형식으로 포맷
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
        String details = results.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    double value = entry.getValue();
                    // 키에 "수익률" 또는 "YTM"이 포함된 경우 퍼센트(%)로 표시
                    if (key.contains("수익률") || key.contains("YTM")) {
                        return String.format("  - %s: %.4f%%", key, value);
                    } else {
                        // 그 외에는 원화(₩)로 표시
                        return String.format("  - %s: %s", key, currencyFormat.format(value));
                    }
                })
                .collect(Collectors.joining("\n"));
        
        // 가장 중요한 지표를 강조하여 한 번 더 보여주기
        String highlight = "";
        if (results.containsKey("실효 수익률 (연, %)")) {
            highlight = String.format("\n✨ 예상 실효 수익률: %.4f%%\n", results.get("실효 수익률 (연, %)"));
        } else if (results.containsKey("만기 수익률 (YTM, %)")) {
            highlight = String.format("\n✨ 예상 만기 수익률: %.4f%%\n", results.get("만기 수익률 (YTM, %)"));
        }

        return String.format("--- %s 계산 결과 ---\n%s%s", productName, details, highlight);
    }
}