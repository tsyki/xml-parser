package jp.gr.java_conf.tsyki.visitor;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * xml内に出現したタグの名称をカウントするVisitor。namespaceの違いは考慮しない
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2016/08/02
 */
public class TagNameCountVisitor implements NodeVisitor {

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

    @Override
    public void afterChildVisit( Node xmlNode) {
        // nothind to do
    }

    private String removeNameSpace( String nodeNameWithNameSpace) {
        final String delimiter = ":";
        if ( !nodeNameWithNameSpace.contains( delimiter)) {
            return nodeNameWithNameSpace;
        }
        String nodeName = nodeNameWithNameSpace.substring( nodeNameWithNameSpace.indexOf( delimiter) + 1, nodeNameWithNameSpace.length());
        return nodeName;
    }

    public Map<String, Long> getCountMap() {
        return countMap;
    }
}
