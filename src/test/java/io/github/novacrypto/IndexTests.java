/*
 *  BIP32derivation
 *  Copyright (C) 2017-2019 Alan Evans, NovaCrypto
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static io.github.novacrypto.bip32.Index.hard;
import static io.github.novacrypto.bip32.Index.isHardened;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public final class IndexTests {

    private final int inputIndex;
    private final int expectedHardened;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {0, 0x80000000},
                {1, 0x80000001},
                {0x0f000001, 0x8f000001},
                {0x0fffffff, 0x8fffffff},
                {0x6abcdef1, 0xeabcdef1},
                {0x7fffffff, 0xffffffff}
        });
    }

    public IndexTests(int inputIndex, int expectedHardened) {
        this.inputIndex = inputIndex;
        this.expectedHardened = expectedHardened;
    }

    @Test
    public void canHarden() {
        assertEquals(expectedHardened, hard(inputIndex));
    }

    @Test
    public void canHardenTwice() {
        assertEquals(expectedHardened, hard(hard(inputIndex)));
    }

    @Test
    public void initialIndexIsNotHardened() {
        assertFalse(isHardened(inputIndex));
    }

    @Test
    public void hardenedIndexIsNotHardened() {
        assertTrue(isHardened(hard(inputIndex)));
    }
}