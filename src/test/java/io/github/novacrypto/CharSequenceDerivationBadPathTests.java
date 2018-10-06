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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Parameterized.class)
public final class CharSequenceDerivationBadPathTests {

    private final String badPath;
    private final String expectedMessage;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][]{
                {"", "Path cannot be empty"},
                {"n", "Path must start with m"},
                {"n/0", "Path must start with m"},
                {"mm0", "Path must start with m/"},
                {"m/1z", "Illegal character in path: z"},
                {"m/1:", "Illegal character in path: :"},
                {"m/ 1", "Illegal character in path:  "},
                {"m/-1", "Illegal character in path: -"},
                {"m/2147483648", "Index number too large"},
        });
    }

    public CharSequenceDerivationBadPathTests(String badPath, String expectedMessage) {
        this.badPath = badPath;
        this.expectedMessage = expectedMessage;
    }

    @Test
    public void expectException() {
        assertThatThrownBy(() ->
                CharSequenceDerivation.INSTANCE.derive(new Node(), badPath, (parent, childIndex) -> new Node())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    private class Node {
    }
}