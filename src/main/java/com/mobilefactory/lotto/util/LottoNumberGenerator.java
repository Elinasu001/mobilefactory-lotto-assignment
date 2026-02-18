package com.mobilefactory.lotto.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class LottoNumberGenerator {

    private static final int MIN_NUMBER = 1;
    private static final int MAX_NUMBER = 99;
    private static final int LOTTO_COUNT = 6;

    /**
     * 랜덤 로또 번호 6개 생성 (중복 없음, 정렬됨)
     * @return "12,23,34,45,56,67" 형식
     */
    public static String generate() {
        Random random = new Random();
        Set<Integer> numbers = new HashSet<>();

        while (numbers.size() < LOTTO_COUNT) {
            int num = MIN_NUMBER + random.nextInt(MAX_NUMBER - MIN_NUMBER + 1);
            numbers.add(num);
        }

        return numbers.stream()
            .sorted()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
    }
}