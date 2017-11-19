/*
 *  BIP32derivation
 *  Copyright (C) 2017 Alan Evans, NovaCrypto
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

package io.github.novacrypto.bip32;

import io.github.novacrypto.bip32.derivation.CharSequenceDerivation;
import io.github.novacrypto.bip32.derivation.IntArrayDerivation;
import org.junit.Test;

public final class EnumValueValueOfCodeCoverageTests {

    private static void superficialEnumCodeCoverage(Class<? extends Enum<?>> enumClass) throws Exception {
        for (Object o : (Object[]) enumClass.getMethod("values").invoke(null)) {
            enumClass.getMethod("valueOf", String.class).invoke(null, o.toString());
        }
    }

    @Test
    public void forCodeCoverageOnly_allEnums() throws Exception {
        superficialEnumCodeCoverage(CharSequenceDerivation.class);
        superficialEnumCodeCoverage(IntArrayDerivation.class);
    }
}