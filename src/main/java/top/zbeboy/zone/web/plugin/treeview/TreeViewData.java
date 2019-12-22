package top.zbeboy.zone.web.plugin.treeview;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class TreeViewData {
    private String text;
    private List<TreeViewData> nodes;
    private String dataId;

    public TreeViewData() {
    }

    public TreeViewData(String text, List<TreeViewData> nodes, String dataId) {
        this.text = text;
        this.nodes = nodes;
        this.dataId = dataId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<TreeViewData> getNodes() {
        return nodes;
    }

    public void setNodes(List<TreeViewData> nodes) {
        this.nodes = nodes;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("nodes", nodes)
                .append("dataId", dataId)
                .toString();
    }
}
