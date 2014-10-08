package org.mongkie.kopath;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author yjjang
 */
@XmlRootElement(name = "pathwaylist")
@XmlSeeAlso(Pathway.class)
public class PathwayList {

    private List<Pathway> pathways = null;

    public PathwayList() {
        this.pathways = new ArrayList<Pathway>();
    }

    public PathwayList(List<Pathway> pathways) {
        this.pathways = pathways;
    }

    public List<Pathway> getPathways() {
        return pathways;
    }

    @XmlAnyElement(lax = true)
    public void setPathways(List<Pathway> pathways) {
        this.pathways = pathways;
    }
}
