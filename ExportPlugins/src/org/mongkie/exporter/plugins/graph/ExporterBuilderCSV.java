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
package org.mongkie.exporter.plugins.graph;

import org.mongkie.exporter.spi.GraphExporterBuilder;
import org.mongkie.util.io.FileType;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Yeongjun Jang <yjjang@kribb.re.kr>
 */
@ServiceProvider(service = GraphExporterBuilder.class, position = 1000)
public class ExporterBuilderCSV implements GraphExporterBuilder<ExporterCSV> {

    @Override
    public FileType[] getFileTypes() {
        return new FileType[]{new FileType("CSV Files", ".csv", ".txt")};
    }

    @Override
    public ExporterCSV buildExporter() {
        return new ExporterCSV();
    }

    @Override
    public String getName() {
        return "CSV";
    }
}