/*
 * This file is part of greenish-jungle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TechShroom Studios <https://techshroom.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.techshroom.jungle;

import static com.google.common.collect.ImmutableSortedSet.toImmutableSortedSet;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;

public class Loaders {

    private static final Function<String, Optional<Integer>> INT = text -> {
        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    };

    public static Function<String, Optional<Integer>> forInt() {
        return INT;
    }

    public static Function<String, Optional<Integer>> forIntInRange(int min, int max) {
        return forInt().andThen(o -> o.filter(i -> min <= i && i < max));
    }

    private static final Function<String, Optional<Long>> LONG = text -> {
        try {
            return Optional.of(Long.parseLong(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    };

    public static Function<String, Optional<Long>> forLong() {
        return LONG;
    }

    public static Function<String, Optional<Long>> forLongInRange(long min, long max) {
        return forLong().andThen(o -> o.filter(l -> min <= l && l < max));
    }

    private static final Function<String, Optional<Double>> DOUBLE = text -> {
        try {
            return Optional.of(Double.parseDouble(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    };

    public static Function<String, Optional<Double>> forDouble() {
        return DOUBLE;
    }

    public static Function<String, Optional<Double>> forDoubleInRange(double min, double max) {
        return forDouble().andThen(o -> o.filter(d -> min <= d && d < max));
    }

    private static final ImmutableSet<String> YES = Stream.of("y", "yes", "1")
            .collect(toImmutableSortedSet(String.CASE_INSENSITIVE_ORDER));

    private static final Function<String, Optional<Boolean>> BOOLEAN = text -> {
        boolean result = Boolean.parseBoolean(text)
                || YES.contains(text);
        return Optional.of(result);
    };

    public static Function<String, Optional<Boolean>> forBoolean() {
        return BOOLEAN;
    }

    private static final Function<String, Optional<String>> STRING = Optional::of;

    public static Function<String, Optional<String>> forString() {
        return STRING;
    }

    public static Function<String, Optional<String>> forStringMatching(Predicate<String> test) {
        return STRING.andThen(o -> o.filter(test));
    }

    private Loaders() {
        throw new AssertionError();
    }

}
