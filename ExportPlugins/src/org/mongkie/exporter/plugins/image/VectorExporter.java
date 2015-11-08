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
package org.mongkie.exporter.plugins.image;

import java.io.IOException;
import org.mongkie.exporter.spi.AbstractImageExporter;
import org.openide.util.Exceptions;
import prefuse.util.display.ImageExporter;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
public class VectorExporter extends AbstractImageExporter {

    private final ImageExporter delegate = new prefuse.util.display.VectorExporter("MONGKIE");

    @Override
    public boolean execute() {
        try {
            return delegate.export(display, out, extension);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedOperationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    @Override
    public boolean cancel() {
        return false;
    }
}