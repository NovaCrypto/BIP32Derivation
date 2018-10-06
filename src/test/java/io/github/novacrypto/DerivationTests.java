/*
 *  BIP32derivation
 *  Copyright (C) 2017-2018 Alan Evans, NovaCrypto
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 *  Original source: https://github.com/NovaCrypto/BIP32derivation
 *  You can contact the authors via github issues.
 */

package io.github.novacrypto;

import io.github.novacrypto.bip32.derivation.CharSequenceDerivation;
import io.github.novacrypto.bip32.derivation.CkdFunctionDerive;
import io.github.novacrypto.bip32.derivation.Derive;
import io.github.novacrypto.bip32.derivation.IntArrayDerivation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.github.novacrypto.bip32.Index.hard;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public final class DerivationTests {

    private final List<Integer> expected;
    private final String path;
    private final int[] list;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {emptyList(), "m"},
                {singletonList(0), "m/0"},
                {singletonList(1), "m/1"},
                {asList(0, 0), "m/0/0"},
                {singletonList(hard(0)), "m/0'"},
                {singletonList(hard(12)), "m/12'"},
                {asList(hard(123), 456), "m/123'/456"},
                {asList(hard(123), 456, 789), "m/123'/456/789"},
                {singletonList(0x7fffffff), "m/" + 0x7fffffff},
                {singletonList(0x7fffffff), "m/2147483647"},
                {singletonList(hard(0x7fffffff)), "m/2147483647'"},
        });
    }

    public DerivationTests(List<Integer> expected, String path) {
        this.expected = expected;
        this.path = path;
        list = copy(expected);
    }

    private int[] copy(List<Integer> expected) {
        final int length = expected.size();
        final int[] list = new int[length];
        for (int i = 0; i < length; i++)
            list[i] = expected.get(i);
        return list;
    }

    @Test
    public void split_by_CharSequenceDerivation() {
        final Integer[] parts = CharSequenceDerivation.INSTANCE
                .derive(new Integer[0], path, DerivationTests::concat);
        final List<Integer> actual = asList(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void split_by_CkdFunctionDerive() {
        final Derive<Integer[]> derive = new CkdFunctionDerive<>(DerivationTests::concat, new Integer[0]);
        final List<Integer> actual = asList(derive.derive(path));
        assertEquals(expected, actual);
    }

    @Test
    public void split_by_CharSequenceDerivation_and_CkdFunctionDerive() {
        final Derive<Integer[]> derive = new CkdFunctionDerive<>(DerivationTests::concat, new Integer[0]);
        final List<Integer> actual = asList(derive.derive(path, CharSequenceDerivation.INSTANCE));
        assertEquals(expected, actual);
    }

    @Test
    public void split_by_IntArrayDerivation() {
        final Integer[] parts = IntArrayDerivation.INSTANCE
                .derive(new Integer[0], list, DerivationTests::concat);
        final List<Integer> actual = asList(parts);
        assertEquals(expected, actual);
    }

    @Test
    public void split_by_IntArrayDerivation_and_CkdFunctionDerive() {
        final Derive<Integer[]> derive = new CkdFunctionDerive<>(DerivationTests::concat, new Integer[0]);
        final List<Integer> actual = asList(derive.derive(list, IntArrayDerivation.INSTANCE));
        assertEquals(expected, actual);
    }

    private static Integer[] concat(Integer[] input, int index) {
        final Integer[] integers = Arrays.copyOf(input, input.length + 1);
        integers[input.length] = index;
        return integers;
    }
}