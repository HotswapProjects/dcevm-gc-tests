/*
 * Copyright (c) 2010, Oracle and/or its affiliates. All rights reserved.
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
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

package com.github.dcevm.test.gc;

import static com.github.dcevm.test.util.HotSwapTestHelper.__toVersion__;
import static com.github.dcevm.test.util.HotSwapTestHelper.__version__;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Complex field test.
 */
public class GCTest {

  public Object[] array;

  public int ver = 0;

  public A a = new A();
  public B b = new B();
  public C c = new C();

  // Version 0
  public static class A {
    public byte byteFld = 10;
    public int test() {
      return 1;
    }
  }

  // Version 1
  public static class A___1 {
    public byte byteFld = 11;
    public short shortFld = 22;
    public int intFld = 33;
    public long longFld = 44L;
    public float floatFld = 55.5F;
    public double doubleFld = 66.6D;
    public char charFld = 'c';
    public boolean booleanFld = false;
    public String stringFld = "NEW";

    // completely new instance fields are below
    public int intComplNewFld = 333;
    public long longComplNewFld = 444L;
    public String stringComplNewFld = "completely new String field";
    public int test() {
      return 2;
    }
  }

  // Version 2
  public static class A___2 {
    public byte byteFld = 10;
    public int test() {
      return 3;
    }
  }

  // Version 0
  public static class B {
    public byte byteFld = 10;
    int test() {
      return 4;
    }
  }

  // Version 0
  public static class B___1 {
    public byte byteFld = 10;
    public long longFld = 44L;
    public float floatFld = 55.5F;
    public int test() {
      return 5;
    }
  }

  // Version 1
  public static class B___2 {
    public byte byteFld = 11;
    public short shortFld = 22;
    public int intFld = 33;
    public long longFld = 44L;
    public float floatFld = 55.5F;
    public double doubleFld = 66.6D;
    public char charFld = 'c';
    public boolean booleanFld = false;
    public String stringFld = "NEW";

    // completely new instance fields are below
    public int intComplNewFld = 333;
    public long longComplNewFld = 444L;
    public String stringComplNewFld = "completely new String field";
    public int test() {
      return 6;
    }
  }

  // Version 0
  public static class C {
    public byte byteFld = 10;
    public int test() {
      return 7;
    }
  }

  // Version 0
  public static class C___1 {
    public float floatFld = 55.5F;
    public int test() {
      return 8;
    }
  }

  // Version 1
  public static class C___2 {
    public String stringComplNewFld = "completely new String field";
    public int test() {
      return 9;
    }
  }

  @Before
  public void setUp() throws Exception {
    __toVersion__(0);
  }

  @Test
  public void testComplexFieldChange() {
    assert __version__() == 0;
    Random rand = new Random(System.currentTimeMillis());
    array = new Object[10000000];
    initArray(array.length, rand, 1);

    ver = 0;
    assertAll();

    for (int i=0; i<1000; i++) {

      System.out.print("To version 1");
      __toVersion__(1);
      ver = 1;
      assertAll();

      System.out.print("To version 2");
      __toVersion__(2);
      ver = 2;
      assertAll();

      System.out.print("To version 0");
      __toVersion__(0);
      ver = 0;
      assertAll();

      if (rand.nextInt() % 10 == 0) {
        initArray(array.length, rand, 10);
      }
    }
  }

  private void assertAll() {
    for (int i=0; i<1000; i++) {
      switch(ver)
      {
      case 0:
        assertEquals(a.test(), 1);
        assertEquals(b.test(), 4);
        assertEquals(c.test(), 7);
        break;
      case 1:
        assertEquals(a.test(), 2);
        assertEquals(b.test(), 5);
        assertEquals(c.test(), 8);
        break;
      case 2:
        assertEquals(a.test(), 3);
        assertEquals(b.test(), 6);
        assertEquals(c.test(), 9);
        break; }
    }

  }

  private void initArray(int length, Random rand, int each) {
    for (int i=0; i < length; i++) {
      if (each != 1 && rand.nextInt() % each != 0) {
        continue;
      }
      if (rand.nextInt() % 6 == 0) {
        array[i] = new A();
        continue;
      }
      if (rand.nextInt() % 4 == 0) {
        array[i] = new B();
        continue;
      }
      if (rand.nextInt() % 3 == 0) {
        array[i] = new C();
        continue;
      }
      array[i] = "Nic";
    }
  }

}
