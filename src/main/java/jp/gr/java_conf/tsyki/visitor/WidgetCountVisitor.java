package jp.gr.java_conf.tsyki.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Node;

/**
 * xml内に出現した部品数をカウントするVisitor
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2016/08/02
 */
public class WidgetCountVisitor implements NodeVisitor {

    /** key:ノード名 value:出現数 */
    private Map<String, Long> countMap = new HashMap<>();

    @Override
    public void visit( Node xmlNode) {
        // コメントは無視
        if ( xmlNode.getNodeType() == Node.COMMENT_NODE) {
            return;
        }
        String nodeNameWithNameSpace = xmlNode.getNodeName();
        String nodeName = removeNameSpace( nodeNameWithNameSpace);

        Long count = countMap.get( nodeName);
        if ( count == null) {
            count = 0L;
        }
        countMap.put( nodeName, count + 1);
    }

    private String removeNameSpace( String nodeNameWithNameSpace) {
        final String delimiter = ":";
        if ( !nodeNameWithNameSpace.contains( delimiter)) {
            return nodeNameWithNameSpace;
        }
        String nodeName = nodeNameWithNameSpace.substring( nodeNameWithNameSpace.indexOf( delimiter) + 1, nodeNameWithNameSpace.length());
        return nodeName;
    }

    public void writeResult() {
        // 出現数の降順でソート
        List<Map.Entry<String, Long>> entries = new ArrayList<Map.Entry<String, Long>>( countMap.entrySet());
        Collections.sort( entries, new Comparator<Map.Entry<String, Long>>() {

            @Override
            public int compare( Entry<String, Long> entry1, Entry<String, Long> entry2) {
                return (( Long) entry2.getValue()).compareTo( ( Long) entry1.getValue());
            }
        });
        for ( Entry<String, Long> entry : entries) {
            String nodeName = entry.getKey();
            Long count = entry.getValue();
            System.out.println( nodeName + " " + count);
        }
    }

    public Map<String, Long> getCountMap() {
        return countMap;
    }
}
