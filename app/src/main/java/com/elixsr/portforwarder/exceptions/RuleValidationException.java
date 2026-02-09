package com.elixsr.portforwarder.exceptions;

import android.content.res.Resources;

/**
 * Created by Cathan on 25/07/2017.
 */
public class RuleValidationException extends ValidationException {

    private final Integer resId;
    private final Object[] formatArgs;

    public RuleValidationException() {
        super();
        this.resId = null;
        this.formatArgs = null;
    }

    public RuleValidationException(String message) {
        super(message);
        this.resId = null;
        this.formatArgs = null;
    }

    public RuleValidationException(int resId, Object... formatArgs) {
        super("");
        this.resId = resId;
        this.formatArgs = formatArgs;
    }

    public RuleValidationException(Throwable cause) {
        super(cause);
        this.resId = null;
        this.formatArgs = null;
    }

    public RuleValidationException(String message, Throwable cause) {
        super(message, cause);
        this.resId = null;
        this.formatArgs = null;
    }

    public boolean hasResourceId() {
        return resId != null;
    }

    public int getResId() {
        return resId;
    }

    public Object[] getFormatArgs() {
        return formatArgs;
    }

    /**
     * Returns localized error message when exception has resource ID, otherwise getMessage().
     */
    public String getLocalizedMessage(Resources res) {
        if (resId != null) {
            if (formatArgs != null && formatArgs.length > 0) {
                return res.getString(resId, formatArgs);
            }
            return res.getString(resId);
        }
        return getMessage();
    }
}
