package de.zib.gndms.logic.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


/**
 * Configuration actions take a string-string map parameter from some external source
 * and may write results to an optional print writer
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 10:36:15
 */
public interface CommandAction<R> extends Action<R> {
    Map<String, String> getLocalOptions();
    void setLocalOptions(final Map<String, String> cfgParams);

    String getLocalOption(final @NotNull String name);

    @NotNull String localOptionsToString();
    void parseLocalOptions(final @NotNull String options)
            throws ParameterTools.ParameterParseException;

    @NotNull String getOption(final @NotNull String name, final @NotNull String def);
    @NotNull String getOption(final @NotNull String name)
        throws MandatoryOptionMissingException;
    @Nullable String getNonMandatoryOption(final @NotNull String name);

    @NotNull Set<String> getAllOptionNames();
    @NotNull Map<String, String> getAllOptions();
    @NotNull String allOptionsToString();

    PrintWriter getPrintWriter();
    boolean hasPrintWriter();
    void setPrintWriter(final PrintWriter writer);

    /**
     * Helper class for parsing string parameters and rendering string-string-maps
     *
     * Supported format (assumes greedy matching):
     *
     * <pre>
     * (params) = (entry [';' (entry)]* [';']
     * (entry) = (ws-char)* (printable-non-ws-char)+{':'|'='} (value)
     * (value) = (ignored-ws-char)* {(trimmed-value) | (exact-value)} (ignored-ws-char)*
     * (trimmed-value) = (pintable-chars-taken-as-value)*
     * (exact-value) = '\'' (pintable-chars-taken-as-value)* '\''
     * </pre>
     *
     * Escpaing is supported in (pintable-chars-taken-as-value) via backspace.
     */
    final class ParameterTools {
        public static final int EXPECTED_MAX_KEY_LENGTH = 32;
        public static final int EXPECTED_MAX_VAL_LENGTH = 64;

        private enum ParseMode
            { KEY_WHITE, KEY, VALUE_BEGIN, TRIMMED_VALUE, EXACT_VALUE, EXACT_VALUE_DONE }

        private ParameterTools() { throw new UnsupportedOperationException("Don't"); }


        @SuppressWarnings({ "HardcodedFileSeparator" })
        public static void parseParameters(final @NotNull Map<String, String> targetMap,
                                           final @NotNull String params)
                throws ParameterParseException {
            String currentKey = "";
            StringBuilder builder = new StringBuilder(Math.max(EXPECTED_MAX_KEY_LENGTH,
                                                               EXPECTED_MAX_VAL_LENGTH));
            ParseMode mode = ParseMode.KEY_WHITE;
            char[] chars = params.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char cur = chars[i];
                if (!Character.isDefined(cur))
                    throw new ParameterParseException("Invalid character in ", params, i);
                switch (mode) {
                    case KEY_WHITE:
                        if (Character.isWhitespace(cur))
                            break;
                        else {
                            builder.delete(0, builder.length());
                            mode = ParseMode.KEY;
                        }
                    case KEY:
                        switch (cur) {
                            case '=':
                            case ':':
                                currentKey = makeKey(builder, i);
                                mode = ParseMode.VALUE_BEGIN;
                                break;
                            default:
                                if (Character.isWhitespace(cur))
                                    throw new ParameterParseException
                                            ("Whitespace between key name and ':'", builder, i);
                                builder.append(cur);
                        }
                        break;
                    case VALUE_BEGIN:
                        switch (cur) {
                            case '\'':
                                mode = ParseMode.EXACT_VALUE;
                                break;
                            default:
                                if (Character.isWhitespace(cur))
                                    break;
                                else {
                                    builder.append(cur);
                                    mode = ParseMode.TRIMMED_VALUE;
                                }
                        }
                        break;
                    case TRIMMED_VALUE:
                        switch (cur) {
                            case ';':
                                makeEntry(targetMap, currentKey, builder, i, true);
                                mode = ParseMode.KEY_WHITE;
                                break;
                            case '\\':
                                cur = escape(builder, chars, i);
                                i++;
                            default:
                                builder.append(cur);
                        }
                        break;
                    case EXACT_VALUE:
                        switch (cur) {
                            case '\'':
                                mode = ParseMode.EXACT_VALUE_DONE;
                                break;
                            case '\\':
                                cur = escape(builder, chars, i);
                                i++;
                            default:
                                builder.append(cur);
                        }
                        break;
                    case EXACT_VALUE_DONE:
                        boolean finished = i + 1 == chars.length;
                        if (! Character.isWhitespace(cur)) {
                            finished = true;
                            // Must look at this char again in next iteration unless ';'
                            if (cur != ';')
                                i--;
                        }
                        if (finished) {
                            makeEntry(targetMap, currentKey, builder, i, false);
                            mode = ParseMode.KEY_WHITE;
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unknown or unhandled ParseMode");
                }
            }
            if (mode == ParseMode.TRIMMED_VALUE
                    || mode == ParseMode.VALUE_BEGIN || mode == ParseMode.EXACT_VALUE_DONE) {
                    makeEntry(targetMap, currentKey, builder, chars.length, true);
                    return;
            }            
            if (mode == ParseMode.KEY_WHITE)
                return;
            else
                throw new ParameterParseException
                        ("Incomplete entry encountered before end of string",
                         params, params.length());
        }


        private static char escape(
                final StringBuilder builderParam, final char[] charsParam, final int index)
                throws ParameterParseException {
            if (index + 1 == charsParam.length)
                throw new ParameterParseException
                        ("Unfinished escape in value", builderParam, index);
            else
                return charsParam[index + 1];
        }


        private static String makeKey(final StringBuilder builderParam, final int iParam)
                throws ParameterParseException {
            final String currentKey;
            currentKey = builderParam.toString();
            if (currentKey.length() == 0)
                throw new ParameterParseException("Key name of size zero", iParam);
            builderParam.delete(0, builderParam.length());
            return currentKey;
        }


        @SuppressWarnings({ "HardcodedLineSeparator" })
        public static void append(final @NotNull StringBuilder builder,
                                  final @NotNull Map<String, String> map, final boolean addNewlines)
        {
            boolean first = true;

            for (Map.Entry<String,String> entry : map.entrySet()) {
                if (first)
                    first = false;
                else
                    { builder.append(';'); builder.append(addNewlines ? '\n' : ' '); }
                String key = entry.getKey();
                String val = entry.getValue();
                for (char c : key.toCharArray()) {
                    if (Character.isWhitespace(c))
                        throw new IllegalArgumentException("Whitespace in key name");
                    if (c == ':' || c == '\'' || c == ';' || c == '=')
                        throw new IllegalArgumentException("Reserved char in key name");
                }
                builder.append(key);
                builder.append(": '");
                final String escaped = val.replaceAll("'", "\'");
                builder.append(escaped);
                builder.append('\'');
            }
        }


        public static void append(final @NotNull StringBuilder builder,
                                  final @NotNull Map<String, String> map) {
            append(builder, map, false);
        }


        public static @NotNull String asString(final @NotNull Map<String, String> map,
            final boolean addNewlines) {
            StringBuilder resultBuilder =
                    new StringBuilder(map.size() *
                            (EXPECTED_MAX_KEY_LENGTH + EXPECTED_MAX_VAL_LENGTH));
            append(resultBuilder, map, addNewlines);
            return resultBuilder.toString();
        }


        public static @NotNull String asString(final @NotNull Map<String, String> map) {
            return asString(map, false);
        }

        private static void makeEntry(
                final @NotNull Map<String, String> targetMap,
                final @NotNull String currentKeyParam,
                final @NotNull StringBuilder valueBuilderParam,
                final int index, final boolean trim)
                throws ParameterParseException {
            String value = trim ? valueBuilderParam.toString().trim() : valueBuilderParam.toString();
            if (targetMap.containsKey(currentKeyParam))
                throw new ParameterParseException
                        ("Duplicate key ", currentKeyParam, index);
            targetMap.put(currentKeyParam, value);
        }


        public static final class ParameterParseException extends Exception {
            private static final long serialVersionUID = 2568606838649522059L;


            public ParameterParseException() {
                super();
            }


            public ParameterParseException(final String message) {
                super(message);
            }


            public ParameterParseException(final String message, final int index) {
                super(message + "(at pos. " + Integer.toString(index) + ')');
            }

            public ParameterParseException(final String message, final String token,
                                           final int index) {
                this(message + " '" + token + "' ", index);
            }


            public ParameterParseException(
                    final String messageParam, final Object builderParam,
                    final int iParam) {
                this (messageParam, builderParam.toString(), iParam);
            }
        }
    }
}
