/*
 * This file is part of MONGKIE. Visit <http://www.mongkie.org/> for details.
 * Copyright (C) 2011 Korean Bioinformation Center(KOBIC)
 * 
 * MONGKIE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MONGKIE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.mongkie.ui.visualization.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.logging.*;
import org.openide.awt.StatusDisplayer;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public final class StatusLogDisplayer extends Handler {

    private StatusLogDisplayer() {
        setFormatter(new SimpleFormatter());
        setFilter(new Filter() {

            @Override
            public boolean isLoggable(LogRecord record) {
                String logger = record.getLoggerName();
                return logger != null && logger.startsWith("org.mongkie.");
            }
        });
        setLevel(Level.INFO);
    }

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        String msg = getFormatter().formatMessage(record);
        StatusDisplayer.getDefault().setStatusText(msg);
        StringBuilder b = new StringBuilder();
        Instant instant = Instant.ofEpochMilli(record.getMillis());
        ZonedDateTime timestamp = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault());
        b.append(timestamp.toLocalTime().toString());
        b.append(' ');
        b.append(msg);
        InputOutput io = IOProvider.getDefault().getIO("Logs", false);
        io.getOut().println(b.toString());
        io.getOut().close();
//        io.select();
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

    public static StatusLogDisplayer getInstance() {
        return Holder.INTSTANCE;
    }

    private static class Holder {

        private static final StatusLogDisplayer INTSTANCE = new StatusLogDisplayer();
    }
}
