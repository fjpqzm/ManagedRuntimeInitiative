/*
 * Copyright 2003-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/**
 * @test
 * @bug 4807942
 * @summary Test the Cipher.getMaxAllowedKeyLength(String) and
 * getMaxAllowedParameterSpec(String) methods
 * @author Valerie Peng
 */

import java.util.*;
import java.nio.*;

import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class GetMaxAllowed {

    private static void runTest(boolean isUnlimited) throws Exception {
        System.out.println("Testing " + (isUnlimited? "un":"") +
                           "limited policy...");

        String algo = "Blowfish";
        int keyLength = Cipher.getMaxAllowedKeyLength(algo);
        AlgorithmParameterSpec spec = Cipher.getMaxAllowedParameterSpec(algo);
        if (isUnlimited) {
            if ((keyLength != Integer.MAX_VALUE) || (spec != null)) {
                throw new Exception("Check for " + algo +
                                    " failed under unlimited policy");
            }
        } else {
            if ((keyLength != 128) || (spec != null)) {
                throw new Exception("Check for " + algo +
                                    " failed under default policy");
            }
        }
        algo = "RC5";
        keyLength = Cipher.getMaxAllowedKeyLength(algo);
        RC5ParameterSpec rc5param = (RC5ParameterSpec)
            Cipher.getMaxAllowedParameterSpec(algo);
        if (isUnlimited) {
            if ((keyLength != Integer.MAX_VALUE) || (rc5param != null)) {
                throw new Exception("Check for " + algo +
                                    " failed under unlimited policy");
            }
        } else {
            if ((keyLength != 128) || (rc5param.getRounds() != 12) ||
                (rc5param.getVersion() != Integer.MAX_VALUE) ||
                (rc5param.getWordSize() != Integer.MAX_VALUE)) {
                throw new Exception("Check for " + algo +
                                    " failed under default policy");
            }
        }
        System.out.println("All tests passed");
    }

    public static void main(String[] args) throws Exception {
        // decide if the installed jurisdiction policy file is the
        // unlimited version
        boolean isUnlimited = true;
        Cipher c = Cipher.getInstance("AES", "SunJCE");
        try {
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(new byte[24], "AES"));
        } catch (InvalidKeyException ike) {
            isUnlimited = false;
        }
        runTest(isUnlimited);
    }
}