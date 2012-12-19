/*
 * Copyright 2008-2013 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zib.gndms.voldmodel.abi;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.JCommander;

/**
 * Executor for an interface and a given number of command line arguments.
 *
 * @author Jšrg Bachmann
 */
public class ABI {
    /**
     * The ABI interface.
     */
    private ABIi iface;
    /**
     * The command line parser.
     */
    private JCommander jc;

    /**
     * Specification whether all options shall be displayed.
     */
    @Parameter(names = "--usage",
           hidden = true)
    private boolean usage;

    /**
     * The command line arguments.
     */
    @Parameter(description = "Commands")
    private List<String> commands = new ArrayList<String>();

    /**
     * Constructs a new ABI with the given interface and arguments.
     * @param iface The interface
     * @param args The arguments
     */
    public ABI(final ABIi iface, final String[] args) {
        this.iface = iface;
        this.jc = new JCommander(this, args);
    }

    /**
     * Executes the parameters in the given interface.
     */
    @SuppressWarnings("unchecked")
    public final void invoke() {
        // we need at least a methods name
        if (commands.size() < 1) {
            return;
        }
        if (usage) {
            jc.usage();
        }
        List<String> commands = new ArrayList<String>(this.commands);
        String cmd = commands.remove(0);

        // use reflect to call ABIi method
        Class<?> c = iface.getClass();
        Method[] methods = c.getDeclaredMethods();

        // search the right method
        for (Method m : methods) {
            // search the right method
            if (Modifier.PUBLIC != m.getModifiers()) {
                continue;
            }
            if (!m.getName().equals(cmd)) {
                continue;
            }
            Class<?>[] paramtypes = m.getParameterTypes();

            // get parameters
            Object[] params = new Object[paramtypes.length];
            for (int i = 0; i < paramtypes.length; ++i) {
                if (paramtypes[i].isAssignableFrom(String.class)) {
                    if (0 == commands.size()) {
                         throw new IllegalArgumentException("The command "
                            + cmd + " expects at least one more argument "
                            + "on method " + m.toGenericString() + ".");
                    }
                    params[i] = commands.remove(0);
                } else if (paramtypes[i].isAssignableFrom(Collection.class)) {
                    Set<String> s = new HashSet<String>();

                    while (commands.size() != 0) {
                        s.add(commands.remove(0));
                    }
                    params[i] = s;
                } else {
                    throw new IllegalStateException("Internal Error: don't "
                          + "understand the parameter type of the API.");
                }
            }

            // call and handle return value
            Object result;
            try {
                result = m.invoke(iface, params);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            // handle results
            if (result instanceof String) {
                String r = (String) result;
                System.out.println(r);
            } else if (result instanceof Collection<?>) {
                Collection<String> r = (Collection<String>) result;
                printCollection(r);
            } else if (result instanceof Map<?, ?>) {
                Map<String, String> r = (Map<String, String>) result;
                printMap(r);
//            } else if (Boolean.TYPE.isInstance(result)) {
//            } else if (Character.TYPE.isInstance(result)) {
//            } else if (Byte.TYPE.isInstance(result)) {
//            } else if (Short.TYPE.isInstance(result)) {
//            } else if (Integer.TYPE.isInstance(result)) {
//            } else if (Long.TYPE.isInstance(result)) {
//            } else if (Float.TYPE.isInstance(result)) {
//            } else if (Double.TYPE.isInstance(result)) {
//            } else if (Void.TYPE.isInstance(result)) {
            }
            return;
        }

        // did not find it - put list of possible methods
        iface.printABICommands();
    }

    /**
     * Prints a collection of Strings one entry per line.
     * @param col The collection
     */
    private static void printCollection(final Collection<String> col) {
        for (String s : col) {
            System.out.println(s);
        }
    }

    /**
     * Prints a map one entry per line, where key and value are
     * separated by ":".
     * @param map The map
     */
    private static void printMap(final Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
