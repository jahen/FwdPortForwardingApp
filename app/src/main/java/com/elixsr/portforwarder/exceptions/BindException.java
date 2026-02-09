/*
 * Fwd: the port forwarding app
 * Copyright (C) 2016  Elixsr Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.elixsr.portforwarder.exceptions;

import android.content.res.Resources;

/**
 * Thrown when an attempt to bind a socket fails.
 */
public class BindException extends Exception {

    private final Integer resId;
    private final Object[] formatArgs;

    public BindException() {
        super();
        this.resId = null;
        this.formatArgs = null;
    }

    public BindException(String detailMessage) {
        super(detailMessage);
        this.resId = null;
        this.formatArgs = null;
    }

    public BindException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        this.resId = null;
        this.formatArgs = null;
    }

    public BindException(Throwable throwable) {
        super(throwable);
        this.resId = null;
        this.formatArgs = null;
    }

    public BindException(int resId, Object... formatArgs) {
        super("");
        this.resId = resId;
        this.formatArgs = formatArgs;
    }

    public BindException(int resId, Object[] formatArgs, Throwable cause) {
        super("", cause);
        this.resId = resId;
        this.formatArgs = formatArgs;
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
