package com.dv.commons.http.impl;

import com.dv.commons.exception.JsonException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * A JSONTokener takes a source string and extracts characters and tokens from
 * it. It is used by the JSONObject and JSONArray constructors to parse
 * JSON source strings.
 *
 * @author JSON.org
 * @version 2014-05-03
 */
public class JsonToken {
    /**
     * current read character position on the current line.
     */
    private long character;
    /**
     * flag to indicate if the end of the input has been found.
     */
    private boolean eof;
    /**
     * current read index of the input.
     */
    private long index;
    /**
     * current line of the input.
     */
    private long line;
    /**
     * previous character read from the input.
     */
    private char previous;
    /**
     * Reader for the input.
     */
    private final Reader reader;
    /**
     * flag to indicate that a previous character was requested.
     */
    private boolean usePrevious;
    /**
     * the number of characters read in the previous line.
     */
    private long characterPreviousLine;


    /**
     * Construct a JSONTokener from a Reader. The caller must close the Reader.
     *
     * @param reader A reader.
     */
    public JsonToken(Reader reader) {
        this.reader = reader.markSupported()
                ? reader
                : new BufferedReader(reader);
        this.eof = false;
        this.usePrevious = false;
        this.previous = 0;
        this.index = 0;
        this.character = 1;
        this.characterPreviousLine = 0;
        this.line = 1;
    }

    /**
     * Construct a JSONTokener from a string.
     *
     * @param s A source string.
     */
    public JsonToken(String s) {
        this(new StringReader(s));
    }


    /**
     * Back up one character. This provides a sort of lookahead capability,
     * so that you can test for a digit or letter before attempting to parse
     * the next number or identifier.
     *
     * @throws JsonException Thrown if trying to step back more than 1 step
     *                       or if already at the start of the string
     */
    public void back() throws JsonException {
        if (this.usePrevious || this.index <= 0) {
            throw new JsonException("Stepping back two steps is not supported");
        }
        this.decrementIndexes();
        this.usePrevious = true;
        this.eof = false;
    }

    /**
     * Decrements the indexes for the {@link #back()} method based on the previous character read.
     */
    private void decrementIndexes() {
        this.index--;
        if (this.previous == '\r' || this.previous == '\n') {
            this.line--;
            this.character = this.characterPreviousLine;
        } else if (this.character > 0) {
            this.character--;
        }
    }

    /**
     * Get the hex value of a character (base16).
     *
     * @param c A character between '0' and '9' or between 'A' and 'F' or
     *          between 'a' and 'f'.
     * @return An int between 0 and 15, or -1 if c was not a hex digit.
     */
    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - ('A' - 10);
        }
        if (c >= 'a' && c <= 'f') {
            return c - ('a' - 10);
        }
        return -1;
    }

    /**
     * Determine if the source string still contains characters that next()
     * can consume.
     *
     * @return true if not yet at the end of the source.
     * @throws JsonException thrown if there is an error stepping forward
     *                       or backward while checking for more data.
     */
    public boolean more() throws JsonException {
        if (this.usePrevious) {
            return true;
        }
        try {
            this.reader.mark(1);
        } catch (IOException e) {
            throw new JsonException("Unable to preserve stream position", e);
        }
        try {
            // -1 is EOF, but next() can not consume the null character '\0'
            if (this.reader.read() <= 0) {
                this.eof = true;
                return false;
            }
            this.reader.reset();
        } catch (IOException e) {
            throw new JsonException("Unable to read the next character from the stream", e);
        }
        return true;
    }


    /**
     * Get the next character in the source string.
     *
     * @return The next character, or 0 if past the end of the source string.
     * @throws JsonException Thrown if there is an error reading the source string.
     */
    public char next() throws JsonException {
        int c;
        if (this.usePrevious) {
            this.usePrevious = false;
            c = this.previous;
        } else {
            try {
                c = this.reader.read();
            } catch (IOException exception) {
                throw new JsonException(exception);
            }
        }
        if (c <= 0) { // End of stream
            this.eof = true;
            return 0;
        }
        this.incrementIndexes(c);
        this.previous = (char) c;
        return this.previous;
    }

    /**
     * Increments the internal indexes according to the previous character
     * read and the character passed as the current character.
     *
     * @param c the current character read.
     */
    private void incrementIndexes(int c) {
        if (c > 0) {
            this.index++;
            if (c == '\r') {
                this.line++;
                this.characterPreviousLine = this.character;
                this.character = 0;
            } else if (c == '\n') {
                if (this.previous != '\r') {
                    this.line++;
                    this.characterPreviousLine = this.character;
                }
                this.character = 0;
            } else {
                this.character++;
            }
        }
    }

    /**
     * Consume the next character, and check that it matches a specified
     * character.
     *
     * @param c The character to match.
     * @return The character.
     * @throws JsonException if the character does not match.
     */
    public char next(char c) throws JsonException {
        char n = this.next();
        if (n != c) {
            if (n > 0) {
                throw this.syntaxError("Expected '" + c + "' and instead saw '" +
                        n + "'");
            }
            throw this.syntaxError("Expected '" + c + "' and instead saw ''");
        }
        return n;
    }

    /**
     * Get the text up but not including the specified character or the
     * end of line, whichever comes first.
     *
     * @param delimiter A delimiter character.
     * @return A string.
     * @throws JsonException Thrown if there is an error while searching
     *                       for the delimiter
     */
    public String nextTo(char delimiter) throws JsonException {
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            char c = this.next();
            if (c == delimiter || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Get the text up but not including one of the specified delimiter
     * characters or the end of line, whichever comes first.
     *
     * @param delimiters A set of delimiter characters.
     * @return A string, trimmed.
     * @throws JsonException Thrown if there is an error while searching
     *                       for the delimiter
     */
    public String nextTo(String delimiters) throws JsonException {
        char c;
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            c = this.next();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    this.back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }


    /**
     * Make a JSONException to signal a syntax error.
     *
     * @param message The error message.
     * @return A JSONException object, suitable for throwing
     */
    public JsonException syntaxError(String message) {
        return new JsonException(message + this);
    }

    /**
     * Make a JSONException to signal a syntax error.
     *
     * @param message  The error message.
     * @param causedBy The throwable that caused the error.
     * @return A JSONException object, suitable for throwing
     */
    public JsonException syntaxError(String message, Throwable causedBy) {
        return new JsonException(message + this.toString(), causedBy);
    }

    /**
     * Make a printable string of this JSONTokener.
     *
     * @return " at {index} [character {character} line {line}]"
     */
    @Override
    public String toString() {
        return " at " + this.index + " [character " + this.character + " line " +
                this.line + "]";
    }
}
